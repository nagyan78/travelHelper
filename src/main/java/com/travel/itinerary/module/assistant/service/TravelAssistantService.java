package com.travel.itinerary.module.assistant.service;

import cn.hutool.core.util.StrUtil;
import com.travel.itinerary.common.util.JwtUtil;
import com.travel.itinerary.common.util.RedisUtil;
import com.travel.itinerary.module.ai.service.AIService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * AI旅行助手服务
 * 提供实时问答、智能推荐等功能
 * 
 * @author Travel Team
 * @version 1.0.0
 */
@Service
@Slf4j
public class TravelAssistantService {
    
    @Autowired
    private AIService aiService;
    
    @Autowired
    private RedisUtil redisUtil;
    
    /**
     * 实时问答
     * 
     * @param userId 用户ID
     * @param question 用户问题
     * @param itineraryId 关联的攻略ID (可选)
     * @return AI回答
     */
    public String askQuestion(Long userId, String question, Long itineraryId) {
        log.info("用户{}提问: {}", userId, question);
        
        // 1. 构建上下文 (关联当前攻略)
        String context = buildContext(itineraryId);
        
        // 2. 检查缓存 (相同问题直接返回)
        String cacheKey = "assistant:qa:" + userId + ":" + md5(question);
        String cached = redisUtil.get(cacheKey);
        if (StrUtil.isNotBlank(cached)) {
            log.debug("命中缓存");
            return cached;
        }
        
        // 3. 调用AI生成回答
        String prompt = String.format(
            "你是一个专业的旅行顾问，擅长提供实用、详细的旅行建议。请基于以下背景信息回答问题。\n\n" +
            "【背景信息】\n%s\n\n" +
            "【用户问题】\n%s\n\n" +
            "【回答要求】\n" +
            "1. 给出具体、可执行的建议\n" +
            "2. 如果涉及地点，提供详细地址和交通方式\n" +
            "3. 如果涉及费用，提供价格范围\n" +
            "4. 语气友好、专业\n" +
            "5. 控制在500字以内",
            StrUtil.isNotBlank(context) ? context : "无特定背景",
            question
        );
        
        String answer = aiService.chat(prompt);
        
        // 4. 缓存结果 (1小时)
        redisUtil.set(cacheKey, answer, 1, TimeUnit.HOURS);
        
        log.info("AI回答完成, 长度: {}", answer.length());
        
        return answer;
    }
    
    /**
     * 智能推荐周边景点
     * 
     * @param latitude 纬度
     * @param longitude 经度
     * @param preference 用户偏好 (可选)
     * @return 推荐景点列表
     */
    public List<String> recommendNearbySpots(
        double latitude, 
        double longitude,
        String preference
    ) {
        log.info("推荐周边景点: lat={}, lng={}, preference={}", latitude, longitude, preference);
        
        // TODO: 集成地图API实现真实推荐
        // 这里提供模拟数据
        
        List<String> recommendations = new ArrayList<>();
        recommendations.add("附近有一个历史博物馆，距离500米，门票免费");
        recommendations.add("步行10分钟可达特色小吃街");
        recommendations.add("附近有共享单车停放点");
        
        if (StrUtil.isNotBlank(preference)) {
            recommendations.add("根据您的偏好'" + preference + "'，推荐附近的文化街区");
        }
        
        return recommendations;
    }
    
    /**
     * 行程优化建议
     * 
     * @param itineraryId 攻略ID
     * @return 优化建议列表
     */
    public List<String> optimizeItinerary(Long itineraryId) {
        log.info("优化行程: itineraryId={}", itineraryId);
        
        // TODO: 基于实际行程数据分析
        List<String> suggestions = new ArrayList<>();
        suggestions.add("第1天行程较紧凑，建议减少1-2个景点");
        suggestions.add("第2天下午有空闲时间，可以添加购物或美食体验");
        suggestions.add("部分景点之间距离较远，建议调整顺序");
        suggestions.add("天气预报显示第3天有雨，建议准备室内活动备选方案");
        
        return suggestions;
    }
    
    /**
     * 常见问题快速回答
     */
    public String getQuickAnswer(String category) {
        Map<String, String> quickAnswers = new HashMap<>();
        
        quickAnswers.put("visa", "签证办理建议:\n1. 提前30天申请\n2. 准备材料:护照、照片、行程单\n3. 可选择电子签或落地签");
        quickAnswers.put("packing", "行李清单:\n✅ 身份证/护照\n✅ 充电器/充电宝\n✅ 常用药品\n✅ 舒适鞋子\n✅ 雨具");
        quickAnswers.put("safety", "安全提示:\n⚠️ 保管好贵重物品\n⚠️ 避免夜间单独出行\n⚠️ 购买旅行保险\n⚠️ 记录紧急联系方式");
        quickAnswers.put("food", "美食推荐原则:\n🍜 选择本地人多的餐厅\n🍜 注意食品卫生\n🍜 尝试当地特色\n🍜 适量品尝，避免不适");
        
        return quickAnswers.getOrDefault(category, "暂无相关信息");
    }
    
    /**
     * 构建上下文信息
     */
    private String buildContext(Long itineraryId) {
        if (itineraryId == null) {
            return "";
        }
        
        // TODO: 从数据库查询攻略详情
        // 这里返回模拟数据
        
        return "用户正在规划北京3日游，已安排的行程包括故宫、长城、颐和园等景点。";
    }
    
    /**
     * MD5加密
     */
    private String md5(String text) {
        return cn.hutool.crypto.digest.DigestUtil.md5Hex(text);
    }
}
