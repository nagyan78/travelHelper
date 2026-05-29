package com.travel.itinerary.module.user.service;

import com.travel.itinerary.module.user.dto.UserLoginDTO;
import com.travel.itinerary.module.user.dto.UserInfoVO;
import com.travel.itinerary.module.user.dto.UserRegisterDTO;

/**
 * 用户服务接口
 * 
 * @author Travel Team
 * @version 2.0.0
 */
public interface UserService {
    
    /**
     * 用户注册
     * 
     * @param dto 注册请求
     * @return 用户信息
     */
    UserInfoVO register(UserRegisterDTO dto);
    
    /**
     * 用户登录
     * 
     * @param dto 登录请求
     * @return Token 信息
     */
    String login(UserLoginDTO dto);
}
