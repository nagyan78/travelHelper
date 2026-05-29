package com.travel.itinerary.module.itinerary.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 攻略信息响应 VO
 * 
 * @author Travel Team
 * @version 2.0.0
 */
@Data
public class ItineraryVO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 攻略ID
     */
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 攻略标题
     */
    private String title;
    
    /**
     * 目的地
     */
    private String destination;
    
    /**
     * 旅行天数
     */
    private Integer days;
    
    /**
     * 总预算
     */
    private BigDecimal totalBudget;
    
    /**
     * 出发日期
     */
    private LocalDate startDate;
    
    /**
     * 状态: 0-草稿, 1-已发布
     */
    private Integer status;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 日程列表
     */
    private List<ItineraryDayVO> dailyPlans;
}
