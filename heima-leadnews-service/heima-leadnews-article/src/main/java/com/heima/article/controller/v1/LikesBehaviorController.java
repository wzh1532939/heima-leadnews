package com.heima.article.controller.v1;

import com.heima.article.service.LikesBehaviorService;
import com.heima.model.article.dtos.LikesBehaviorDto;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class LikesBehaviorController {
    @Autowired
    private LikesBehaviorService likesBehaviorService;
    @PostMapping("/likes_behavior")
    public ResponseResult Upvote(@RequestBody LikesBehaviorDto dto){
        return likesBehaviorService.Upvote(dto);
    }
}
