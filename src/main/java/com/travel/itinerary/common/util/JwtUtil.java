package com.travel.itinerary.common.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 工具类
 * 
 * 负责 JWT Token 的生成、解析和验证
 * 使用 jjwt 0.11.5 版本
 * 
 * @author Travel Team
 * @version 2.0.0
 */
@Slf4j
@Component
public class JwtUtil {
    
    /**
     * JWT 密钥 (从配置文件读取)
     */
    @Value("${jwt.secret}")
    private String secret;
    
    /**
     * Token 过期时间 (秒，从配置文件读取)
     */
    @Value("${jwt.expiration}")
    private Long expiration;
    
    /**
     * 获取签名密钥
     * 
     * @return SecretKey 对象
     */
    private SecretKey getSignKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
    /**
     * 生成 JWT Token
     * 
     * @param userId 用户ID
     * @param username 用户名
     * @return JWT Token 字符串
     */
    public String generateToken(Long userId, String username) {
        // 构建 Claims
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        
        // 计算过期时间
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + expiration * 1000);
        
        // 生成 Token
        String token = Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(expireDate)
            .signWith(getSignKey(), SignatureAlgorithm.HS256)
            .compact();
        
        log.info("生成 JWT Token: userId={}, username={}", userId, username);
        
        return token;
    }
    
    /**
     * 解析 JWT Token
     * 
     * @param token JWT Token 字符串
     * @return Claims 对象
     * @throws ExpiredJwtException Token 已过期
     * @throws MalformedJwtException Token 格式错误
     * @throws SignatureException 签名验证失败
     */
    public Claims parseToken(String token) {
        try {
            return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        } catch (ExpiredJwtException e) {
            log.warn("JWT Token 已过期: {}", e.getMessage());
            throw e;
        } catch (MalformedJwtException e) {
            log.warn("JWT Token 格式错误: {}", e.getMessage());
            throw e;
        } catch (SignatureException e) {
            log.warn("JWT Token 签名验证失败: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("JWT Token 解析失败: {}", e.getMessage());
            throw new RuntimeException("Token 解析失败", e);
        }
    }
    
    /**
     * 从 Token 中提取用户ID
     * 
     * @param token JWT Token 字符串
     * @return 用户ID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.get("userId", Long.class);
    }
    
    /**
     * 从 Token 中提取用户名
     * 
     * @param token JWT Token 字符串
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.get("username", String.class);
    }
    
    /**
     * 验证 Token 是否有效
     * 
     * @param token JWT Token 字符串
     * @return true-有效，false-无效
     */
    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (Exception e) {
            log.debug("JWT Token 验证失败: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * 判断 Token 是否已过期
     * 
     * @param token JWT Token 字符串
     * @return true-已过期，false-未过期
     */
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = parseToken(token);
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }
    
    /**
     * 从当前请求上下文中获取用户ID
     * 需要在拦截器中先将 userId 设置到 request attribute 中
     * 
     * @return 用户ID，如果未登录则返回 null
     */
    public static Long getCurrentUserId() {
        try {
            ServletRequestAttributes attributes = 
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes == null) {
                return null;
            }
            HttpServletRequest request = attributes.getRequest();
            return (Long) request.getAttribute("userId");
        } catch (Exception e) {
            log.warn("获取当前用户ID失败: {}", e.getMessage());
            return null;
        }
    }
}