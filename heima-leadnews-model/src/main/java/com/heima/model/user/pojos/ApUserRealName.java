package com.heima.model.user.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * APP用户信息表
 * </p>
 *
 * @author itheima
 */
@Data
@TableName("ap_user_realname")
public class  ApUserRealName implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    @TableField("user_id")
    private Integer userId;
    private String name;
    private String idno;
    @TableField("font_image")
    private String fontImage;
    @TableField("back_image")
    private String idnobackImage;
    @TableField("hold_image")
    private String holdImage;
    @TableField("live_image")
    private String liveImage;
    private Integer status;
    private String reason;
    @TableField("created_time")
    private Date createdTime;
    @TableField("submited_time")
    private Date submitedTime;
    @TableField("updated_time")
    private Date updatedTime;


}