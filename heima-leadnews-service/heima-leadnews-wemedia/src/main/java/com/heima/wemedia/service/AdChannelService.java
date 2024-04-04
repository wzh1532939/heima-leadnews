package com.heima.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.ChannelDto;
import com.heima.model.wemedia.dtos.SensitivePageReqDto;
import com.heima.model.wemedia.pojos.WmChannel;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface AdChannelService extends IService<WmChannel> {
    public ResponseResult delChannel(Integer id);
    public ResponseResult findChannel(SensitivePageReqDto dto);
    public ResponseResult saveChannel(ChannelDto dto);
    public ResponseResult updateChannel(ChannelDto dto);
}
