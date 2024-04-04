package com.heima.model.wemedia.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class ChannelDto {
    private Integer id;
    private Integer ord;
    private Date createTime;
    private String description;
    private String name;
    private boolean isDefault;
    private boolean status;
}
