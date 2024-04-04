package com.heima.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.user.dtos.AuthDto;
import com.heima.model.user.pojos.ApUser;
import com.heima.model.user.pojos.ApUserRealName;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface ApUserAuthService extends IService<ApUserRealName> {
    public ResponseResult findUser(AuthDto dto);
    public ResponseResult authFailUser( AuthDto dto);
    @PostMapping("/authPass")
    public ResponseResult authPassUser(AuthDto dto);
}
