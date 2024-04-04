package com.heima.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.admin.dtos.AdLoginDto;
import com.heima.model.admin.pojos.AdUser;
import com.heima.model.common.dtos.ResponseResult;

public interface AdminLoginService extends IService<AdUser> {
    /**
     * 登录
     * @param dto
     * @return
     */
    public ResponseResult login(AdLoginDto dto);
}
