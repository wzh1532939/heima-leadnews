package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.injector.methods.SelectById;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.dtos.NewsAuthDto;
import com.heima.model.wemedia.pojos.WmNews;
import com.heima.wemedia.mapper.WemediaAuthMapper;
import com.heima.wemedia.service.WemediaAuthService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class WemediaAuthServiceImpl extends ServiceImpl<WemediaAuthMapper, WmNews> implements WemediaAuthService {
    @Override
    public ResponseResult findWemediaAuth(NewsAuthDto dto) {
        dto.checkParam();
        IPage page=new Page(dto.getPage(),dto.getSize());
        LambdaQueryWrapper<WmNews> lambdaQueryWrapper=new LambdaQueryWrapper();
        if(dto.getId()!=null){
            lambdaQueryWrapper.eq(WmNews::getId,dto.getId());
        }
        if(StringUtils.isNotBlank(dto.getMsg())){
            lambdaQueryWrapper.eq(WmNews::getReason,dto.getMsg());
        }
        if(dto.getStatus()!=null){
            lambdaQueryWrapper.eq(WmNews::getStatus,dto.getStatus());
        }
        if(StringUtils.isNotBlank(dto.getTitle())){
            lambdaQueryWrapper.eq(WmNews::getTitle,dto.getTitle());
        }
        lambdaQueryWrapper.orderByDesc(WmNews::getPublishTime);
        page= page(page, lambdaQueryWrapper);
        ResponseResult responseResult=new PageResponseResult(dto.getPage(),dto.getSize(), (int) page.getTotal());
        responseResult.setData(page.getRecords());
        return responseResult;
    }

    @Override
    public ResponseResult checkDetail(Integer id) {
        if(id==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        WmNews wmNews=getById(id);
        return ResponseResult.okResult(wmNews);
    }

    @Override
    public ResponseResult WemediaAuthFail(NewsAuthDto dto) {
        WmNews wmNews=new WmNews();
        if(dto.getId()!=null){
            wmNews.setId(dto.getId());
        }
        if(dto.getStatus()!=null){
            wmNews.setStatus(dto.getStatus());
        }
        if(StringUtils.isNotBlank(dto.getTitle()))
        {
            wmNews.setTitle(dto.getTitle());
        }
        if (StringUtils.isNotBlank(dto.getMsg())){
            wmNews.setReason(dto.getMsg());
        }
        updateById(wmNews);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult WemediaAuthPass(NewsAuthDto dto) {
        WmNews wmNews=new WmNews();
        if(dto.getId()!=null){
            wmNews.setId(dto.getId());
        }
        if(dto.getStatus()!=null){
            wmNews.setStatus(dto.getStatus());
        }
        if(StringUtils.isNotBlank(dto.getTitle()))
        {
            wmNews.setTitle(dto.getTitle());
        }
        if (StringUtils.isNotBlank(dto.getMsg())){
            wmNews.setReason(dto.getMsg());
        }
        updateById(wmNews);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
