package com.travel.itinerary.module.user.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户信息响应 VO
 * 
 * @author Travel Team
 * @version 2.0.0
 */
@Data
public class UserInfoVO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 用户ID
     */
    private Long id;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 昵称
     */
    private String nickname;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 手机号
     */
    private String phone;
    
    /**
     * 头像URL
     */
    private String avatar;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
