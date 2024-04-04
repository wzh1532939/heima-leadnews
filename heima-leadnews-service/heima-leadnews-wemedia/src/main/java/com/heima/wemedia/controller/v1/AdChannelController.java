package com.heima.wemedia.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.ChannelDto;
import com.heima.model.wemedia.dtos.SensitivePageReqDto;
import com.heima.wemedia.service.AdChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/channel")
public class AdChannelController {
    @Autowired
    private AdChannelService adChannelService;

    /**
     * 删除频道
     * @param id
     * @return
     */
    @GetMapping("/del/{id}")
    public ResponseResult delChannel(@PathVariable Integer id){
        return adChannelService.delChannel(id);
    }

    /**
     * 模糊查询频道
     * @param dto
     * @return
     */
    @PostMapping("/list")
    public ResponseResult findChannel(@RequestBody SensitivePageReqDto dto){
        return adChannelService.findChannel(dto);
    }
    /**
     * 保存频道
     * @param dto
     * @return
     */
    @PostMapping("/save")
    public ResponseResult saveChannel(@RequestBody ChannelDto dto){
        return adChannelService.saveChannel(dto);
    }
    /**
     * 修改频道
     * @param dto
     * @return
     */
    @PostMapping("/update")
    public ResponseResult updateChannel(@RequestBody ChannelDto dto){
        return adChannelService.updateChannel(dto);
    }
}
