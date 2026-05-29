package com.travel.itinerary.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 错误码枚举类
 * 
 * 定义系统中所有业务异常的错误码和消息
 * 
 * @author Travel Team
 * @version 2.0.0
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {
    
    // --- 通用错误 (1xxx) ---
    SUCCESS(200, "操作成功"),
    PARAM_INVALID(400, "参数校验失败"),
    UNAUTHORIZED(401, "未授权，请先登录"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),
    SYSTEM_ERROR(500, "系统内部错误"),
    
    // --- Token 相关错误 (11xx) ---
    TOKEN_INVALID(1101, "Token 无效或格式错误"),
    TOKEN_EXPIRED(1102, "Token 已过期"),
    
    // --- 用户模块错误 (2xxx) ---
    USERNAME_EXISTS(2001, "用户名已存在"),
    EMAIL_EXISTS(2002, "邮箱已被注册"),
    PHONE_EXISTS(2003, "手机号已被注册"),
    USER_NOT_FOUND(2004, "用户不存在"),
    PASSWORD_WRONG(2005, "密码错误"),
    ACCOUNT_DISABLED(2006, "账号已被禁用"),
    
    // --- 攻略模块错误 (3xxx) ---
    ITINERARY_NOT_FOUND(3001, "攻略不存在"),
    ITINERARY_NO_PERMISSION(3002, "无权操作该攻略"),
    DATE_INVALID(3003, "日期格式无效"),
    BUDGET_INVALID(3004, "预算金额无效"),
    
    // --- AI 服务错误 (4xxx) ---
    AI_SERVICE_ERROR(4001, "AI 服务调用失败"),
    AI_EMPTY_RESPONSE(4002, "AI 返回内容为空"),
    AI_TIMEOUT(4003, "AI 服务响应超时"),
    
    // --- 第三方服务错误 (5xxx) ---
    WEATHER_API_ERROR(5001, "天气接口调用失败"),
    MAP_API_ERROR(5002, "地图接口调用失败");
    
    /**
     * 错误码
     */
    private final Integer code;
    
    /**
     * 错误消息
     */
    private final String message;
}
