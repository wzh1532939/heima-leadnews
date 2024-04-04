package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.pojos.WmUser;
import com.heima.wemedia.mapper.WmUserMapper;
import com.heima.wemedia.service.AdWemediaUserService;
import org.springframework.stereotype.Service;

@Service
public class AdWemediaUserServiceImpl extends ServiceImpl<WmUserMapper, WmUser> implements AdWemediaUserService {
    @Override
    public ResponseResult addWemediaUser(WmUser dto) {
        save(dto);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
