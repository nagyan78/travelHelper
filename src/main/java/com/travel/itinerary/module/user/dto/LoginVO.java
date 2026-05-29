package com.travel.itinerary.module.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 登录响应 VO
 * 
 * @author Travel Team
 * @version 2.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginVO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * JWT Token
     */
    private String token;
    
    /**
     * 过期时间 (秒)
     */
    private Long expiresIn;
}
