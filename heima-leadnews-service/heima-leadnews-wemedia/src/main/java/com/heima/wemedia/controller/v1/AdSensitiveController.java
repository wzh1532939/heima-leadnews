package com.heima.wemedia.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.SensitivePageReqDto;
import com.heima.model.wemedia.dtos.WmSensitiveDto;
import com.heima.wemedia.service.AdSensitiveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/v1/sensitive")
public class AdSensitiveController {
    @Autowired
    private AdSensitiveService sensitiveService;

    /**
     * 删除敏感词
     * @param id
     * @return
     */
    @DeleteMapping("/del/{id}")
    public ResponseResult delSensitive(@PathVariable Integer id){
        return sensitiveService.delSensitive(id);
    }

    /**
     * 查询敏感词
     * @return
     */
    @PostMapping("/list")
    public ResponseResult findSensitive(@RequestBody SensitivePageReqDto dto){
        return sensitiveService.findSensitive(dto);
    }

    /**
     * 保存敏感词
     * @param dto
     * @return
     */
    @PostMapping("/save")
    public ResponseResult saveSensitive(@RequestBody WmSensitiveDto dto){
        return sensitiveService.saveSensitive(dto);
    }

    /**
     * 修改敏感词
     * @param dto
     * @return
     */
    @PostMapping("/update")
    public ResponseResult updateSensitive(@RequestBody WmSensitiveDto dto){
        return sensitiveService.updateSensitive(dto);
    }
}
