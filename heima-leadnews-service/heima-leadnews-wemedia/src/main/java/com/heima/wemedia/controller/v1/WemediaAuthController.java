package com.heima.wemedia.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.NewsAuthDto;
import com.heima.wemedia.service.WemediaAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/news")
public class WemediaAuthController {
    @Autowired
    private WemediaAuthService wemediaAuthService;

    /**
     *  查询文章列表
     * @param dto
     * @return
     */
    @PostMapping("/list_vo")
    public ResponseResult findWemediaAuth(@RequestBody NewsAuthDto dto){
        return wemediaAuthService.findWemediaAuth(dto);
    }

    /**
     * 查询文章详情
     * @param id
     * @return
     */
    @GetMapping("/one_vo/{id}")
    public ResponseResult checkDetail(@PathVariable Integer id)
    {
        return wemediaAuthService.checkDetail(id);
    }

    /**
     * 文章审核失败
     * @param dto
     * @return
     */
    @PostMapping("/auth_fail")
    public ResponseResult WemediaAuthFail(@RequestBody NewsAuthDto dto){
        return wemediaAuthService.WemediaAuthFail(dto);
    }

    /**
     * 文章审核成功
     * @param dto
     * @return
     */
    @PostMapping("/auth_pass")
    public ResponseResult WemediaAuthPass(@RequestBody NewsAuthDto dto){
        return wemediaAuthService.WemediaAuthPass(dto);
    }
}
