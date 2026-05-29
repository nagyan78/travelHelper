package com.travel.itinerary.module.user.service.impl;

import com.travel.itinerary.common.exception.BusinessException;
import com.travel.itinerary.common.exception.ErrorCode;
import com.travel.itinerary.common.util.JwtUtil;
import com.travel.itinerary.module.user.dto.UserLoginDTO;
import com.travel.itinerary.module.user.dto.UserInfoVO;
import com.travel.itinerary.module.user.dto.UserRegisterDTO;
import com.travel.itinerary.module.user.entity.User;
import com.travel.itinerary.module.user.mapper.UserMapper;
import com.travel.itinerary.module.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 用户服务实现类
 * 
 * @author Travel Team
 * @version 2.0.0
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    @Override
    @Transactional
    public UserInfoVO register(UserRegisterDTO dto) {
        log.info("用户注册: {}", dto.getUsername());
        
        // 1. 校验用户名唯一性
        if (userMapper.existsByUsername(dto.getUsername())) {
            throw new BusinessException(ErrorCode.USERNAME_EXISTS);
        }
        
        // 2. 创建用户对象
        User user = new User();
        BeanUtils.copyProperties(dto, user);
        
        // 3. 密码加密
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        
        // 4. 设置默认值
        user.setStatus(1);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        
        // 5. 插入数据库
        userMapper.insert(user);
        
        log.info("用户注册成功: {}", user.getId());
        
        // 6. 转换为 VO
        UserInfoVO vo = new UserInfoVO();
        BeanUtils.copyProperties(user, vo);
        
        return vo;
    }
    
    @Override
    public String login(UserLoginDTO dto) {
        log.info("用户登录: {}", dto.getUsername());
        
        // 1. 查询用户
        User user = userMapper.findByUsername(dto.getUsername());
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        
        // 2. 校验状态
        if (user.getStatus() == 0) {
            throw new BusinessException(ErrorCode.ACCOUNT_DISABLED);
        }
        
        // 3. 校验密码
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.PASSWORD_WRONG);
        }
        
        // 4. 生成 Token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());
        
        log.info("用户登录成功: {}", user.getId());
        
        return token;
    }
}
