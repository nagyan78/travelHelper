package com.travel.itinerary.module.itinerary.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 攻略主表实体类
 * 
 * @author Travel Team
 * @version 2.0.0
 */
@Data
@TableName("t_itinerary")
public class Itinerary implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
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
     * 偏好权重 JSON 字符串
     */
    private String preferenceWeights;
    
    /**
     * 状态: 0-草稿, 1-已发布
     */
    private Integer status;
    
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


