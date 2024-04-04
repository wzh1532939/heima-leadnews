package com.heima.user.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.user.dtos.AuthDto;
import com.heima.user.service.ApUserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class ApUserAuthController {
    @Autowired
    private ApUserAuthService apUserAuthService;

    /**
     * 查询所有用户
     * @param dto
     * @return
     */
    @PostMapping("list")
    public ResponseResult findUser(@RequestBody AuthDto dto){
        return apUserAuthService.findUser(dto);
    }
    @PostMapping("/authFail")
    public ResponseResult authFailUser(@RequestBody AuthDto dto){
        return apUserAuthService.authFailUser(dto);
    }
    @PostMapping("/authPass")
    public ResponseResult authPassUser(@RequestBody AuthDto dto){
        return apUserAuthService.authPassUser(dto);
    }
}
