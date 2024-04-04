package com.heima.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.NewsAuthDto;
import com.heima.model.wemedia.pojos.WmNews;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface WemediaAuthService extends IService<WmNews> {
    public ResponseResult findWemediaAuth( NewsAuthDto dto);
    public ResponseResult checkDetail( Integer id);
    public ResponseResult WemediaAuthFail( NewsAuthDto dto);
    public ResponseResult WemediaAuthPass( NewsAuthDto dto);
}
