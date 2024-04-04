package com.heima.apis.user;

import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.pojos.WmUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "leadnews-wemedia",contextId="IWmUserClient")
public interface IWmUserClient {

    @PostMapping("/api/v1/add")
    public ResponseResult addWemediaUser(@RequestBody WmUser dto);
}