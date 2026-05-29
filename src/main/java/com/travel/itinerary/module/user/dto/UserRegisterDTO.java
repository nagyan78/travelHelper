package com.travel.itinerary.module.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 用户注册请求 DTO
 * 
 * @author Travel Team
 * @version 2.0.0
 */
@Data
public class UserRegisterDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 用户名 (3-20位)
     */
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 20, message = "用户名长度必须在3-20之间")
    private String username;
    
    /**
     * 密码 (6-20位)
     */
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20之间")
    private String password;
    
    /**
     * 邮箱 (可选)
     */
    @Email(message = "邮箱格式不正确")
    private String email;
    
    /**
     * 手机号 (可选，中国大陆手机号)
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;
}
