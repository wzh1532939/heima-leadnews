package com.heima.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.pojos.WmUser;
import org.springframework.web.bind.annotation.RequestBody;

public interface AdWemediaUserService extends IService<WmUser> {
    public ResponseResult addWemediaUser(WmUser dto);
}
