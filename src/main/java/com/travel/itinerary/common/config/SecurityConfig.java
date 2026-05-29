package com.travel.itinerary.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security 配置类
 * 
 * @author Travel Team
 * @version 2.0.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()  // 前后端分离，禁用 CSRF
            .authorizeRequests()
                // 公开接口（无需认证）
                .antMatchers("/api/user/login", "/api/user/register").permitAll()
                .antMatchers("/api/analyzer/**").permitAll()
                .antMatchers("/api/budget/estimate", "/api/budget/allocation").permitAll()
                .antMatchers("/actuator/health").permitAll()
                // 其他接口需要认证
                .anyRequest().authenticated()
            .and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);  // 无状态会话
        
        return http.build();
    }
}
