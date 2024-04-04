package com.heima.wemedia.controller.v1;

import com.heima.apis.user.IWmUserClient;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.pojos.WmUser;
import com.heima.wemedia.service.AdWemediaUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/v1")
public class AdWemediaUserController implements IWmUserClient {
    @Autowired
    private AdWemediaUserService adWemediaUserService;
    @PostMapping("/add")
    public ResponseResult addWemediaUser(@RequestBody WmUser dto){
        return adWemediaUserService.addWemediaUser(dto);
    }
}
