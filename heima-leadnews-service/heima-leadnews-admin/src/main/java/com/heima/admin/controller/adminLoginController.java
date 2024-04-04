package com.heima.admin.controller;

import com.heima.model.admin.dtos.AdLoginDto;
import com.heima.model.common.dtos.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.heima.admin.service.AdminLoginService;

@RestController
@Slf4j
public class adminLoginController {
    @Autowired
    private AdminLoginService adminLoginService;
    @PostMapping("/login/in")
    public ResponseResult login(@RequestBody AdLoginDto dto){
        return adminLoginService.login(dto);
    }
}
