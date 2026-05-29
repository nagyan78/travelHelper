package com.travel.itinerary.module.ai.service;

import com.travel.itinerary.module.analyzer.dto.DimensionWeights;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * AI 提示词模板构建器
 * 
 * @author Travel Team
 * @version 2.0.0
 */
@Slf4j
@Component
public class PromptTemplate {
    
    /**
     * 构建增强版 Prompt
     * 
     * @param destination 目的地
     * @param days 旅行天数
     * @param budget 总预算
     * @param weights 用户偏好权重
     * @return 完整的 Prompt 字符串
     */
    public String buildEnhancedPrompt(String destination, int days, BigDecimal budget, DimensionWeights weights) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("你是一个专业的旅游攻略规划师，请根据以下信息生成一份详细的旅游攻略。\n\n");
        
        // 基本信息
        sb.append("【基本信息】\n");
        sb.append("目的地: ").append(destination).append("\n");
        sb.append("旅行天数: ").append(days).append("天\n");
        sb.append("总预算: ").append(budget).append("元\n\n");
        
        // 用户偏好分析
        sb.append("【用户偏好分析】\n");
        sb.append("根据用户描述，AI分析出以下偏好权重（0-100分）:\n");
        sb.append("- 美食指数: ").append(weights.getFoodIndex()).append("\n");
        sb.append("- 文化深度: ").append(weights.getCultureDepth()).append("\n");
        sb.append("- 自然风光: ").append(weights.getNatureScenery()).append("\n");
        sb.append("- 休闲程度: ").append(weights.getLeisureLevel()).append("\n");
        sb.append("- 亲子友好: ").append(weights.getParentChild()).append("\n");
        sb.append("- 摄影需求: ").append(weights.getPhotography()).append("\n");
        sb.append("- 购物倾向: ").append(weights.getShopping()).append("\n");
        sb.append("- 探险精神: ").append(weights.getAdventure()).append("\n\n");
        
        // 生成要求 (动态添加)
        sb.append("【生成要求】\n");
        if (weights.getFoodIndex() > 70) {
            sb.append("- 重点推荐当地特色美食，每天至少安排2次特色餐饮\n");
        }
        if (weights.getCultureDepth() > 70) {
            sb.append("- 优先安排博物馆、古迹等文化景点\n");
        }
        if (weights.getNatureScenery() > 70) {
            sb.append("- 增加自然景观游览时间，推荐最佳观景时段\n");
        }
        if (weights.getLeisureLevel() > 70) {
            sb.append("- 行程安排要宽松，每天不超过3个主要景点，预留休息时间\n");
        }
        if (weights.getParentChild() > 70) {
            sb.append("- 选择适合亲子的景点和活动，注意安全性与趣味性\n");
        }
        if (weights.getPhotography() > 70) {
            sb.append("- 推荐拍照打卡点，标注最佳拍摄时间和机位建议\n");
        }
        if (weights.getShopping() > 70) {
            sb.append("- 安排当地特色商圈或免税店行程\n");
        }
        if (weights.getAdventure() > 70) {
            sb.append("- 增加徒步、登山等户外探险活动\n");
        }
        
        sb.append("\n请以JSON格式返回攻略内容，包含以下字段:\n");
        sb.append("- title: 攻略标题\n");
        sb.append("- days: 天数\n");
        sb.append("- dailyPlans: 每日行程数组\n");
        sb.append("  - dayNumber: 第几天\n");
        sb.append("  - date: 日期\n");
        sb.append("  - spots: 景点/活动数组\n");
        sb.append("    - title: 名称\n");
        sb.append("    - type: 类型 (spot/food/traffic/free)\n");
        sb.append("    - startTime: 开始时间\n");
        sb.append("    - endTime: 结束时间\n");
        sb.append("    - location: 地点\n");
        sb.append("    - cost: 费用\n");
        sb.append("    - notes: 备注\n");
        
        return sb.toString();
    }
}
