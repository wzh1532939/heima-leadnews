package com.heima.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.apis.user.IWmUserClient;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.user.dtos.AuthDto;
import com.heima.model.user.pojos.ApUser;
import com.heima.model.user.pojos.ApUserRealName;
import com.heima.model.wemedia.pojos.WmChannel;
import com.heima.model.wemedia.pojos.WmUser;
import com.heima.user.mapper.ApUserAuthMapper;
import com.heima.user.service.ApUserAuthService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApUserAuthServiceImpl extends ServiceImpl<ApUserAuthMapper, ApUserRealName> implements ApUserAuthService {
    @Override
    public ResponseResult findUser(AuthDto dto) {
        dto.checkParam();
        IPage page=new Page(dto.getPage(),dto.getSize());
        LambdaQueryWrapper<ApUserRealName> lambdaQueryWrapper=new LambdaQueryWrapper<ApUserRealName>();
        if(dto.getStatus()!=null){
            lambdaQueryWrapper.eq(ApUserRealName::getStatus,dto.getStatus());
        }
        if(StringUtils.isNotBlank(dto.getMsg())){
            lambdaQueryWrapper.eq(ApUserRealName::getReason,dto.getMsg());
        }
        lambdaQueryWrapper.orderByDesc(ApUserRealName::getSubmitedTime);
        page = page(page, lambdaQueryWrapper);
        ResponseResult responseResult=new PageResponseResult(dto.getPage(),dto.getSize(), (int) page.getTotal());
        responseResult.setData(page.getRecords());
        return responseResult;
    }

    @Override
    public ResponseResult authFailUser(AuthDto dto) {
        ApUserRealName apUserRealName=new ApUserRealName();
        apUserRealName.setId(dto.getId());
        apUserRealName.setStatus(2);
        apUserRealName.setReason(dto.getMsg());
        updateById(apUserRealName);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Autowired
    private IWmUserClient iWmUserClient;
    @Override
    public ResponseResult authPassUser(AuthDto dto) {
        ApUserRealName apUserRealName=new ApUserRealName();
        apUserRealName.setId(dto.getId());
        apUserRealName.setStatus(9);
        updateById(apUserRealName);
        apUserRealName  = getById(apUserRealName.getId());
        WmUser user=new WmUser();
        BeanUtils.copyProperties(apUserRealName,user);
        iWmUserClient.addWemediaUser(user);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
