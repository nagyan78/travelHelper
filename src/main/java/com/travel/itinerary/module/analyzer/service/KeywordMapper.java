package com.travel.itinerary.module.analyzer.service;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 关键词映射规则库
 * 
 * 将自然语言关键词映射到 8 个偏好维度及对应的分数贡献
 * 
 * @author Travel Team
 * @version 2.0.0
 */
public class KeywordMapper {
    
    /**
     * 关键词 -> 维度权重 映射表
     */
    public static final Map<String, DimensionWeight> KEYWORD_RULES = new HashMap<>();
    
    static {
        // --- 美食相关 ---
        addRule("美食", "foodIndex", 90);
        addRule("好吃", "foodIndex", 85);
        addRule("餐厅", "foodIndex", 80);
        addRule("小吃", "foodIndex", 75);
        addRule("特色菜", "foodIndex", 85);
        addRule("夜市", "foodIndex", 80);
        
        // --- 文化相关 ---
        addRule("博物馆", "cultureDepth", 95);
        addRule("历史", "cultureDepth", 90);
        addRule("文化", "cultureDepth", 85);
        addRule("古迹", "cultureDepth", 90);
        addRule("人文", "cultureDepth", 85);
        addRule("展览", "cultureDepth", 80);
        
        // --- 自然相关 ---
        addRule("自然", "natureScenery", 90);
        addRule("风景", "natureScenery", 85);
        addRule("山水", "natureScenery", 90);
        addRule("公园", "natureScenery", 80);
        addRule("海边", "natureScenery", 85);
        addRule("森林", "natureScenery", 85);
        
        // --- 休闲相关 ---
        addRule("轻松", "leisureLevel", 90);
        addRule("休闲", "leisureLevel", 85);
        addRule("慢", "leisureLevel", 80);
        addRule("度假", "leisureLevel", 90);
        addRule("放松", "leisureLevel", 85);
        
        // --- 亲子相关 ---
        addRule("孩子", "parentChild", 95);
        addRule("亲子", "parentChild", 90);
        addRule("家庭", "parentChild", 85);
        addRule("乐园", "parentChild", 90);
        addRule("动物园", "parentChild", 90);
        
        // --- 摄影相关 ---
        addRule("拍照", "photography", 90);
        addRule("打卡", "photography", 85);
        addRule("网红", "photography", 80);
        addRule("出片", "photography", 90);
        addRule("景色", "photography", 80);
        
        // --- 购物相关 ---
        addRule("购物", "shopping", 90);
        addRule("商场", "shopping", 85);
        addRule("特产", "shopping", 75);
        addRule("逛街", "shopping", 80);
        addRule("免税店", "shopping", 85);
        
        // --- 探险相关 ---
        addRule("探险", "adventure", 95);
        addRule("刺激", "adventure", 90);
        addRule("冒险", "adventure", 90);
        addRule("徒步", "adventure", 85);
        addRule("登山", "adventure", 85);
    }
    
    /**
     * 添加规则辅助方法
     */
    private static void addRule(String keyword, String dimension, int score) {
        KEYWORD_RULES.put(keyword, new DimensionWeight(dimension, score));
    }
    
    /**
     * 内部类：维度权重定义
     */
    @Data
    @AllArgsConstructor
    public static class DimensionWeight {
        private String dimension;
        private int score;
    }
}
