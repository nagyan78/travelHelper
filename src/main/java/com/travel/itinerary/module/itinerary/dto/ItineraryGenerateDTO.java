package com.travel.itinerary.module.itinerary.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 攻略生成请求 DTO
 * 
 * @author Travel Team
 * @version 2.0.0
 */
@Data
public class ItineraryGenerateDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 目的地
     */
    @NotBlank(message = "目的地不能为空")
    private String destination;
    
    /**
     * 旅行天数
     */
    @NotNull(message = "旅行天数不能为空")
    @Min(value = 1, message = "旅行天数至少为1天")
    private Integer days;
    
    /**
     * 总预算
     */
    @NotNull(message = "预算不能为空")
    private BigDecimal totalBudget;
    
    /**
     * 出发日期
     */
    @NotNull(message = "出发日期不能为空")
    private LocalDate startDate;
    
    /**
     * 用户自然语言描述 (用于偏好分析)
     */
    private String userInput;
}
