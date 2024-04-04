package com.heima.wemedia.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.heima.apis.article.IArticleClient;

import com.heima.common.aliyun.GreenImageScan;
import com.heima.common.aliyun.GreenTextScan;
import com.heima.common.constants.ExamineResult;
import com.heima.common.test4j.Tess4jClient;
import com.heima.file.service.FileStorageService;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.pojos.WmChannel;
import com.heima.model.wemedia.pojos.WmNews;
import com.heima.model.wemedia.pojos.WmSensitive;
import com.heima.model.wemedia.pojos.WmUser;
import com.heima.utils.common.SensitiveWordUtil;
import com.heima.wemedia.mapper.WmChannelMapper;
import com.heima.wemedia.mapper.WmNewsMapper;
import com.heima.wemedia.mapper.WmSensitiveMapper;
import com.heima.wemedia.mapper.WmUserMapper;
import com.heima.wemedia.service.WmNewsAutoScanService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WmNewsAutoScanServiceImpl implements WmNewsAutoScanService {
    @Autowired
    private WmNewsMapper wmNewsMapper;
    @Override
    @Async
    public void autoScanWmnews(Integer id) {
        //查询文章
        WmNews wmNews = wmNewsMapper.selectById(id);
        if(wmNews==null){
            throw new RuntimeException("WmNewsAutoScanServiceImpl-文章不存在");
        }
        if(wmNews.getStatus().equals(WmNews.Status.SUBMIT.getCode())){
            //从内容提取文本内容和图片
            Map<String,Object> textAndImages=handlerTextAndImages(wmNews);
            //自管理的敏感词过滤
            boolean isSensitive=handleSensitiveScan((String) textAndImages.get("content"),wmNews);
            if(!isSensitive) return;
            //审核文章
            boolean isTextScan=handleTextScan((String) textAndImages.get("content"),wmNews);
            if(!isTextScan){
                return;
            }
            //审核图片
            boolean isImageScan=handleImageScan((List<String>) textAndImages.get("images"),wmNews);
            if(!isImageScan){
                return;
            }
            //审核成功，保存文章
            ResponseResult responseResult = saveAppArticle(wmNews);
            if(!responseResult.getCode().equals(200)){
                throw new RuntimeException("WmNewsAutoScanServiceImpl-文章审核，保存app相关数据·失败");
            }
            //回填article_id
            wmNews.setArticleId((Long) responseResult.getData());
            updateWmNews(wmNews, (short) 9,"审核成功");
        }
    }
    @Autowired
    private WmSensitiveMapper wmSensitiveMapper;

    /**
     * 自管理的敏感词审核
     * @param content
     * @param wmNews
     * @return
     */
    private boolean handleSensitiveScan(String content, WmNews wmNews) {

        boolean flag = true;

        //获取所有的敏感词
        List<WmSensitive> wmSensitives = wmSensitiveMapper.selectList(Wrappers.<WmSensitive>lambdaQuery().select(WmSensitive::getSensitives));
        List<String> sensitiveList = wmSensitives.stream().map(WmSensitive::getSensitives).collect(Collectors.toList());

        //初始化敏感词库
        SensitiveWordUtil.initMap(sensitiveList);

        //查看文章中是否包含敏感词
        Map<String, Integer> map = SensitiveWordUtil.matchWords(content);
        if(map.size() >0){
            updateWmNews(wmNews,(short) 2,"当前文章中存在违规内容"+map);
            flag = false;
        }

        return flag;
    }

    @Autowired
    private IArticleClient articleClient;
    @Autowired
    private WmChannelMapper wmChannelMapper;

    @Autowired
    private WmUserMapper wmUserMapper;

    /**
     * 保存app端相关的文章数据
     * @param wmNews
     */
    private ResponseResult saveAppArticle(WmNews wmNews) {

        ArticleDto dto = new ArticleDto();
        //属性的拷贝
        BeanUtils.copyProperties(wmNews,dto);
        //文章的布局
        dto.setLayout(wmNews.getType());
        //频道
        WmChannel wmChannel = wmChannelMapper.selectById(wmNews.getChannelId());
        if(wmChannel != null){
            dto.setChannelName(wmChannel.getName());
        }

        //作者
        dto.setAuthorId(wmNews.getUserId().longValue());
        WmUser wmUser = wmUserMapper.selectById(wmNews.getUserId());
        if(wmUser != null){
            dto.setAuthorName(wmUser.getName());
        }

        //设置文章id
        if(wmNews.getArticleId() != null){
            dto.setId(wmNews.getArticleId());
        }
        dto.setCreatedTime(new Date());

        ResponseResult responseResult = articleClient.saveArticle(dto);
        return responseResult;

    }
    @Autowired
    private GreenImageScan greenImageScan;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private Tess4jClient tess4jClient;
    /**
     * 审核图片
     * @param images
     * @param wmNews
     * @return
     */
    private boolean handleImageScan(List<String> images, WmNews wmNews) {
        boolean flag=true;
        if(images==null||images.size()==0){
            return flag;
        }
        images = images.stream().distinct().collect(Collectors.toList());
        for (String image : images) {
            byte[] bytes = fileStorageService.downLoadFile(image);
            ByteArrayInputStream in=new ByteArrayInputStream(bytes);
            try {
                BufferedImage bufferedImage = ImageIO.read(in);
                String result = tess4jClient.doOCR(bufferedImage);
                boolean isSensitive = handleSensitiveScan(result, wmNews);
                if(!isSensitive) return isSensitive;
                ExamineResult map = greenImageScan.imageScan(bytes);
                if(map.getConclusion()!=null){
                    if(map.getConclusion().equals("不合规")){
                        flag=false;
                        updateWmNews(wmNews, (short) 3,"当前文章有不确定内容");
                    }
                }
            } catch (Exception e) {
                flag=false;
                throw new RuntimeException(e);
            }
        }
        return flag;
    }

    @Autowired
    private GreenTextScan greenTextScan;
    private boolean handleTextScan(String content, WmNews wmNews) {
        boolean flag=true;
        if((wmNews.getTitle()+"-"+content).length()==0){
            return true;
        }
        try {
            ExamineResult map = greenTextScan.TextScan(wmNews.getTitle()+"-"+content);
            if(map.getConclusion()!=null&&map.getConclusionType()!=null){
                if(map.getConclusion().equals("不合规")||map.getConclusionType().equals("2")){
                    flag=false;
                    updateWmNews(wmNews, (short) 2,"当前文章有违规内容");
                }
                if(map.getConclusion().equals("不合规")&&map.getConclusionType().equals("3")){
                    flag=false;
                    updateWmNews(wmNews, (short) 3,"当前文章有不确定内容");
                }
            }
        } catch (Exception e) {
            flag=false;
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 修改文章内容
     * @param wmNews
     */
    private void updateWmNews(WmNews wmNews,Short status,String reason) {
        wmNews.setStatus(status);
        wmNews.setReason(reason);
        wmNewsMapper.updateById(wmNews);
    }

    //从自媒体内容提取文本内容和图片
    private Map<String, Object> handlerTextAndImages(WmNews wmNews) {
        //存储纯文本内容
        StringBuilder stringBuilder=new StringBuilder();
        List<String> images=new ArrayList<>();
        if(StringUtils.isNotBlank(wmNews.getContent())){
            List<Map> maps = JSON.parseArray(wmNews.getContent(), Map.class);
            for (Map map : maps) {
                if(map.get("type").equals("text")){
                    stringBuilder.append(map.get("value"));
                }
                if(map.get("type").equals("image")){
                    images.add((String) map.get("value"));
                }
            }
        }
        if(StringUtils.isNotBlank(wmNews.getImages())){
            String[] split = wmNews.getImages().split(",");
            images.addAll(Arrays.asList(split));
        }
        Map<String, Object> resultMap=new HashMap<>();
        resultMap.put("content",stringBuilder.toString());
        resultMap.put("images",images);

        return resultMap;
    }
}
