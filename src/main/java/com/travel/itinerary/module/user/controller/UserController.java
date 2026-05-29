package com.travel.itinerary.module.user.controller;

import com.travel.itinerary.common.result.Result;
import com.travel.itinerary.module.user.dto.LoginVO;
import com.travel.itinerary.module.user.dto.UserLoginDTO;
import com.travel.itinerary.module.user.dto.UserInfoVO;
import com.travel.itinerary.module.user.dto.UserRegisterDTO;
import com.travel.itinerary.module.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 * 
 * @author Travel Team
 * @version 2.0.0
 */
@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @Value("${jwt.expiration}")
    private Long jwtExpiration;
    
    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<UserInfoVO> register(@Validated @RequestBody UserRegisterDTO dto) {
        UserInfoVO vo = userService.register(dto);
        return Result.success(vo);
    }
    
    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<LoginVO> login(@Validated @RequestBody UserLoginDTO dto) {
        String token = userService.login(dto);
        LoginVO vo = new LoginVO(token, jwtExpiration);
        return Result.success(vo);
    }
}
