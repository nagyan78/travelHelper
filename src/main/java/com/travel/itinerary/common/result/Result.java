package com.travel.itinerary.common.result;

import lombok.Data;

/**
 * 统一返回结果封装类
 * 
 * @param <T> 数据类型
 * @author Travel Team
 * @version 2.0.0
 */
@Data
public class Result<T> {
    
    /**
     * 状态码
     * 200 - 成功
     * 400 - 参数错误
     * 401 - 未授权
     * 403 - 禁止访问
     * 404 - 资源不存在
     * 500 - 服务器内部错误
     */
    private Integer code;
    
    /**
     * 响应消息
     */
    private String message;
    
    /**
     * 响应数据
     */
    private T data;
    
    /**
     * 成功返回（带数据）
     * 
     * @param data 响应数据
     * @param <T> 数据类型
     * @return Result对象
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("success");
        result.setData(data);
        return result;
    }
    
    /**
     * 成功返回（无数据）
     * 
     * @param <T> 数据类型
     * @return Result对象
     */
    public static <T> Result<T> success() {
        return success(null);
    }
    
    /**
     * 失败返回
     * 
     * @param code 错误码
     * @param message 错误消息
     * @param <T> 数据类型
     * @return Result对象
     */
    public static <T> Result<T> error(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
    
    /**
     * 判断是否成功
     * 
     * @return true-成功，false-失败
     */
    public boolean isSuccess() {
        return this.code != null && this.code == 200;
    }
}
