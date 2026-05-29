package com.travel.itinerary.module.itinerary.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 攻略日程明细实体类
 * 
 * @author Travel Team
 * @version 2.0.0
 */
@Data
@TableName("t_itinerary_day")
public class ItineraryDay implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 攻略ID
     */
    private Long itineraryId;
    
    /**
     * 第几天
     */
    private Integer dayNumber;
    
    /**
     * 日期
     */
    private java.time.LocalDate date;
    
    /**
     * 日程内容 JSON 字符串 (包含景点、餐饮、交通等)
     */
    private String content;
    
    /**
     * 当日预算
     */
    private BigDecimal dailyBudget;
    
    /**
     * 排序号
     */
    private Integer sortOrder;
    
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    /**
     * 逻辑删除: 0-未删除, 1-已删除
     */
    @TableLogic
    private Integer isDeleted;
}


