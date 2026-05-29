package com.travel.itinerary.module.analyzer.service;

import com.travel.itinerary.module.analyzer.dto.DimensionWeights;

/**
 * 关键词分析服务接口
 * 
 * @author Travel Team
 * @version 2.0.0
 */
public interface KeywordAnalyzerService {
    
    /**
     * 分析用户输入，提取偏好权重
     * 
     * @param userInput 用户自然语言描述
     * @return 8个维度的权重分布
     */
    DimensionWeights analyze(String userInput);
}
