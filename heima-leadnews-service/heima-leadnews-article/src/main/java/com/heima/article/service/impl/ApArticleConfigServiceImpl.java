package com.heima.article.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.article.mapper.ApArticleConfigMapper;
import com.heima.article.service.ApArticleConfigService;
import com.heima.model.article.pojos.ApArticleConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Slf4j
@Transactional
public class ApArticleConfigServiceImpl extends  ServiceImpl<ApArticleConfigMapper,ApArticleConfig> implements ApArticleConfigService{
    /**
     * 修改文章
     * @param map
     */
    @Override
    public void updateByMap(Map map) {
        boolean isDown=true;
        Object enable=map.get("enable");
        if(enable.equals(1)){
            isDown=false;
        }
        update(Wrappers.<ApArticleConfig> lambdaUpdate().eq(ApArticleConfig::getArticleId,map.get("articleId"))
                .set(ApArticleConfig::getIsDown,isDown));
    }
}
