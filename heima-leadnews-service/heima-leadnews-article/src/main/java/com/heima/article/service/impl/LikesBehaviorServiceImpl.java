package com.heima.article.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.article.mapper.ApArticleMapper;
import com.heima.article.service.LikesBehaviorService;
import com.heima.model.article.dtos.LikesBehaviorDto;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class LikesBehaviorServiceImpl extends ServiceImpl<ApArticleMapper, ApArticle> implements LikesBehaviorService {
    @Override
    public ResponseResult Upvote(LikesBehaviorDto dto) {
        if(dto.getArticleId()==null){
            return ResponseResult.okResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        ApArticle apArticle = getById(dto.getArticleId());
        if(dto.getOperation()==1&&apArticle.getLikes()>0){
            apArticle.setLikes(apArticle.getLikes()-1);
        }
        if(dto.getOperation()==0){
            apArticle.setLikes(apArticle.getLikes()+1);
        }
        updateById(apArticle);
        return ResponseResult.okResult(apArticle);
    }
}
