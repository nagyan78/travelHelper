package com.travel.itinerary.common.config;

import com.travel.itinerary.common.exception.BusinessException;
import com.travel.itinerary.common.exception.ErrorCode;
import com.travel.itinerary.common.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JWT 拦截器
 * 
 * @author Travel Team
 * @version 2.0.0
 */
@Slf4j
@Component
public class JwtInterceptor implements HandlerInterceptor {
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取 Token
        String token = request.getHeader("Authorization");
        
        if (token == null || !token.startsWith("Bearer ")) {
            throw new BusinessException(ErrorCode.TOKEN_INVALID);
        }
        
        // 去除 "Bearer " 前缀
        token = token.substring(7);
        
        // 验证 Token
        if (!jwtUtil.validateToken(token)) {
            throw new BusinessException(ErrorCode.TOKEN_EXPIRED);
        }
        
        // 提取用户信息并存入请求属性
        Long userId = jwtUtil.getUserIdFromToken(token);
        request.setAttribute("userId", userId);
        
        log.debug("用户认证通过: userId={}", userId);
        return true;
    }
}
