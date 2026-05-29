package com.travel.itinerary.module.itinerary.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 景点/活动响应 VO
 * 
 * @author Travel Team
 * @version 2.0.0
 */
@Data
public class SpotVO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 名称
     */
    private String title;
    
    /**
     * 类型 (spot/food/traffic/free)
     */
    private String type;
    
    /**
     * 开始时间
     */
    private String startTime;
    
    /**
     * 结束时间
     */
    private String endTime;
    
    /**
     * 地点
     */
    private String location;
    
    /**
     * 费用
     */
    private BigDecimal cost;
    
    /**
     * 备注
     */
    private String notes;
}
