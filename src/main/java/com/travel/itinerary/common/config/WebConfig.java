package com.travel.itinerary.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 配置类
 * 
 * @author Travel Team
 * @version 2.0.0
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Autowired
    private JwtInterceptor jwtInterceptor;
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加 JWT 拦截器，拦截所有 /api/** 路径
        registry.addInterceptor(jwtInterceptor)
            .addPathPatterns("/api/**")
            // 排除登录注册接口
            .excludePathPatterns("/api/user/login", "/api/user/register")
            // 排除偏好分析接口（无需认证）
            .excludePathPatterns("/api/analyzer/**")
            // 排除预算估算接口（无需认证）
            .excludePathPatterns("/api/budget/estimate", "/api/budget/allocation");
    }
}
