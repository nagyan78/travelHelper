package com.travel.itinerary.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.travel.itinerary.common.result.Result;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

/**
 * 全局异常处理器
 * 
 * 统一处理Controller层抛出的各类异常，返回标准化的错误响应
 * 
 * @author Travel Team
 * @version 2.0.0
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * 处理业务异常
     * 
     * @param e 业务异常
     * @return 错误响应结果
     */
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e) {
        log.warn("业务异常: {}", e.getMessage());
        return Result.error(e.getCode(), e.getMessage());
    }
    
    /**
     * 处理参数校验异常 (@Valid/@RequestBody)
     * 
     * @param e 方法参数校验异常
     * @return 错误响应结果
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        // 获取第一个校验失败的字段和消息
        FieldError fieldError = e.getBindingResult().getFieldError();
        String message = fieldError != null ? fieldError.getDefaultMessage() : "参数校验失败";
        
        log.warn("参数校验失败: {}", message);
        return Result.error(ErrorCode.PARAM_INVALID.getCode(), message);
    }
    
    /**
     * 处理参数绑定异常 (@ModelAttribute)
     * 
     * @param e 绑定异常
     * @return 错误响应结果
     */
    @ExceptionHandler(BindException.class)
    public Result<Void> handleBindException(BindException e) {
        FieldError fieldError = e.getFieldError();
        String message = fieldError != null ? fieldError.getDefaultMessage() : "参数绑定失败";
        
        log.warn("参数绑定失败: {}", message);
        return Result.error(ErrorCode.PARAM_INVALID.getCode(), message);
    }
    
    /**
     * 处理约束违反异常 (单个参数校验)
     * 
     * @param e 约束违反异常
     * @return 错误响应结果
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<Void> handleConstraintViolationException(ConstraintViolationException e) {
        String message = e.getConstraintViolations().stream()
            .map(ConstraintViolation::getMessage)
            .collect(Collectors.joining(", "));
        
        log.warn("约束违反: {}", message);
        return Result.error(ErrorCode.PARAM_INVALID.getCode(), message);
    }
    
    /**
     * 处理非法参数异常
     * 
     * @param e 非法参数异常
     * @return 错误响应结果
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Result<Void> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("非法参数: {}", e.getMessage());
        return Result.error(ErrorCode.PARAM_INVALID.getCode(), e.getMessage());
    }
    
    /**
     * 处理空指针异常
     * 
     * @param e 空指针异常
     * @return 错误响应结果
     */
    @ExceptionHandler(NullPointerException.class)
    public Result<Void> handleNullPointerException(NullPointerException e) {
        log.error("空指针异常", e);
        return Result.error(ErrorCode.SYSTEM_ERROR.getCode(), "系统内部错误");
    }
    
    /**
     * 处理所有未捕获的未知异常
     * 
     * @param e 未知异常
     * @return 错误响应结果
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        log.error("系统异常: ", e);
        return Result.error(ErrorCode.SYSTEM_ERROR.getCode(), "系统内部错误，请稍后重试");
    }
}
