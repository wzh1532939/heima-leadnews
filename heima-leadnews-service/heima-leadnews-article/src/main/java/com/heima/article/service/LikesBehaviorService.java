package com.heima.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.article.dtos.LikesBehaviorDto;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.web.bind.annotation.RequestBody;

public interface LikesBehaviorService extends IService<ApArticle> {
    public ResponseResult Upvote(LikesBehaviorDto dto);
}
