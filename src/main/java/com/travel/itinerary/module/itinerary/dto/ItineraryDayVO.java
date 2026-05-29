package com.travel.itinerary.module.itinerary.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 攻略日程响应 VO
 * 
 * @author Travel Team
 * @version 2.0.0
 */
@Data
public class ItineraryDayVO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 第几天
     */
    private Integer dayNumber;
    
    /**
     * 日期
     */
    private LocalDate date;
    
    /**
     * 当日预算
     */
    private BigDecimal dailyBudget;
    
    /**
     * 景点/活动列表 (解析自 content JSON)
     */
    private List<SpotVO> spots;
}
