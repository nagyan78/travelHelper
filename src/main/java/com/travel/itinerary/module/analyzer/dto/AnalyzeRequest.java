package com.travel.itinerary.module.analyzer.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 偏好分析请求 DTO
 * 
 * @author Travel Team
 * @version 2.0.0
 */
@Data
public class AnalyzeRequest implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 用户自然语言描述
     */
    @NotBlank(message = "用户描述不能为空")
    private String userInput;
}
