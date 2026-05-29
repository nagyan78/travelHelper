package com.travel.itinerary.module.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户实体类
 * 
 * @author Travel Team
 * @version 2.0.0
 */
@Data
@TableName("t_user")
public class User implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 密码 (BCrypt加密)
     */
    private String password;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 手机号
     */
    private String phone;
    
    /**
     * 昵称
     */
    private String nickname;
    
    /**
     * 头像URL
     */
    private String avatar;
    
    /**
     * 性别: 0-未知, 1-男, 2-女
     */
    private Integer gender;
    
    /**
     * 生日
     */
    private java.time.LocalDate birthday;
    
    /**
     * 个性签名
     */
    private String signature;
    
    /**
     * 状态: 0-禁用, 1-正常
     */
    private Integer status;
    
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    /**
     * 逻辑删除: 0-未删除, 1-已删除
     */
    @TableLogic
    private Integer isDeleted;
}


