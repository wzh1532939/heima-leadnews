package com.heima.model.admin.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@TableName("ad_user")
public class AdUser {
    @TableId(value = "id",type = IdType.ID_WORKER)
    private Integer id;
    private String name;
    private String password;
    private String salt;
    private String nickname;
    private String image;
    private String phone;
    private Short status;
    private String email;
    @TableField("login_time")
    private Date loginTime;
    @TableField("created_time")
    private Date createdTime;
}
