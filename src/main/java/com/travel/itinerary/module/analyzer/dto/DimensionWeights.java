package com.travel.itinerary.module.analyzer.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 维度权重 DTO
 * 
 * 用于存储用户旅行偏好的 8 个核心维度权重（0-100分）
 * 
 * @author Travel Team
 * @version 2.0.0
 */
@Data
public class DimensionWeights implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 美食指数 (Food Index)
     */
    private Integer foodIndex = 50;
    
    /**
     * 文化深度 (Culture Depth)
     */
    private Integer cultureDepth = 50;
    
    /**
     * 自然风光 (Nature Scenery)
     */
    private Integer natureScenery = 50;
    
    /**
     * 休闲程度 (Leisure Level)
     */
    private Integer leisureLevel = 50;
    
    /**
     * 亲子友好 (Parent-Child Friendly)
     */
    private Integer parentChild = 50;
    
    /**
     * 摄影需求 (Photography Demand)
     */
    private Integer photography = 50;
    
    /**
     * 购物倾向 (Shopping Tendency)
     */
    private Integer shopping = 50;
    
    /**
     * 探险精神 (Adventure Spirit)
     */
    private Integer adventure = 50;
    
    /**
     * 获取权重最高的维度名称
     * 
     * @return 维度名称字符串 (如: "美食", "文化")
     */
    public String getTopDimension() {
        int max = foodIndex;
        String name = "美食";
        
        if (cultureDepth > max) { max = cultureDepth; name = "文化"; }
        if (natureScenery > max) { max = natureScenery; name = "自然"; }
        if (leisureLevel > max) { max = leisureLevel; name = "休闲"; }
        if (parentChild > max) { max = parentChild; name = "亲子"; }
        if (photography > max) { max = photography; name = "摄影"; }
        if (shopping > max) { max = shopping; name = "购物"; }
        if (adventure > max) { max = adventure; name = "探险"; }
        
        return name;
    }
}
