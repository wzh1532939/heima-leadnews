package com.heima.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.SensitivePageReqDto;
import com.heima.model.wemedia.dtos.WmSensitiveDto;
import com.heima.model.wemedia.pojos.WmSensitive;


public interface AdSensitiveService extends IService<WmSensitive> {
    /**
     * 删除敏感词
     * @param id
     * @return
     */
    public ResponseResult delSensitive(Integer id);
    /**
     * 查询敏感词
     * @return
     */
    public ResponseResult findSensitive(SensitivePageReqDto dto);
    /**
     * 保存敏感词
     * @return
     */
    public ResponseResult saveSensitive( WmSensitiveDto dto);
    /**
     * 修改敏感词
     * @return
     */
    public ResponseResult updateSensitive(WmSensitiveDto dto);
}
