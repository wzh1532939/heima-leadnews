package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.dtos.SensitivePageReqDto;
import com.heima.model.wemedia.dtos.WmSensitiveDto;
import com.heima.model.wemedia.pojos.WmSensitive;
import com.heima.wemedia.mapper.AdSensitiveMapper;
import com.heima.wemedia.service.AdSensitiveService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;


@Service
public class AdSensitiveServiceImpl extends ServiceImpl<AdSensitiveMapper,WmSensitive> implements AdSensitiveService {
    @Override
    public ResponseResult delSensitive(Integer id) {
        //检查参数
        if(id==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        removeById(id);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult findSensitive(SensitivePageReqDto dto) {
        dto.checkParam();
        IPage page=new Page(dto.getPage(),dto.getSize());
        LambdaQueryWrapper<WmSensitive> lambdaQueryWrapper=new LambdaQueryWrapper<WmSensitive>();
        if(StringUtils.isNotBlank(dto.getName())){
            lambdaQueryWrapper.eq(WmSensitive::getSensitives,dto.getName());
        }
        lambdaQueryWrapper.orderByDesc(WmSensitive::getCreatedTime);
        page = page(page, lambdaQueryWrapper);
        ResponseResult responseResult=new PageResponseResult(dto.getPage(),dto.getSize(), (int) page.getTotal());
        responseResult.setData(page.getRecords());
        return responseResult;
    }

    @Override
    public ResponseResult saveSensitive(WmSensitiveDto dto) {
        WmSensitive wmSensitive=new WmSensitive();
        if(dto.getId()!=null){
            wmSensitive.setId(dto.getId());
        }
        if(dto.getCreatedTime()!=null){
            wmSensitive.setCreatedTime(dto.getCreatedTime());
        }
        if(StringUtils.isNotBlank(dto.getSensitives())){
            wmSensitive.setSensitives(dto.getSensitives());
        }
        save(wmSensitive);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult updateSensitive(WmSensitiveDto dto) {
        WmSensitive wmSensitive=new WmSensitive();
        if(dto.getId()!=null){
            wmSensitive.setId(dto.getId());
        }
        if(dto.getCreatedTime()!=null){
            wmSensitive.setCreatedTime(dto.getCreatedTime());
        }
        if(StringUtils.isNotBlank(dto.getSensitives())){
            wmSensitive.setSensitives(dto.getSensitives());
        }
        updateById(wmSensitive);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
