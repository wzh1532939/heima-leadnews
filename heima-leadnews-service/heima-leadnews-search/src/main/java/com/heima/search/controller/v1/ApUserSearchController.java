package com.heima.search.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.search.dtos.HistorySearchDto;
import com.heima.search.service.ApUserSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/v1/history")
public class ApUserSearchController {
    @Autowired
    ApUserSearchService apUserSearchService;
    @PostMapping("/load")
    public ResponseResult findUserSearch(){

        return apUserSearchService.findUserSearch();
    }
    @PostMapping("/del")
    public ResponseResult delUserSearch(@RequestBody HistorySearchDto dto){
        return  apUserSearchService.delUserSearch(dto);
    }
}
