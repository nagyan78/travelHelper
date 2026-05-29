package com.travel.itinerary.module.analyzer.service.impl;

import cn.hutool.core.util.StrUtil;
import com.travel.itinerary.module.analyzer.dto.DimensionWeights;
import com.travel.itinerary.module.analyzer.service.KeywordAnalyzerService;
import com.travel.itinerary.module.analyzer.service.KeywordMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 关键词分析服务实现类
 * 
 * @author Travel Team
 * @version 2.0.0
 */
@Slf4j
@Service
public class KeywordAnalyzerServiceImpl implements KeywordAnalyzerService {
    
    /**
     * 停用词表 (简化版)
     */
    private static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(
        "我", "的", "了", "和", "是", "在", "有", "很", "也", "不", "想", "要", "去", "一个", "一些"
    ));
    
    @Override
    public DimensionWeights analyze(String userInput) {
        log.info("开始分析用户输入: {}", userInput);
        
        // 1. 空输入处理：返回默认权重
        if (StrUtil.isBlank(userInput)) {
            log.warn("用户输入为空，返回默认权重");
            return getDefaultWeights();
        }
        
        // 2. 文本预处理
        String cleanedText = preprocess(userInput);
        
        // 3. 关键词提取
        List<String> keywords = extractKeywords(cleanedText);
        log.debug("提取关键词: {}", keywords);
        
        // 4. 计算原始分数
        Map<String, Integer> rawScores = calculateRawScores(keywords);
        
        // 5. 归一化处理
        DimensionWeights weights = normalize(rawScores);
        
        log.info("权重分析完成: {}", weights);
        return weights;
    }
    
    /**
     * 文本预处理
     */
    private String preprocess(String text) {
        // 去除标点符号
        String cleaned = text.replaceAll("[\\p{Punct}]", " ");
        // 转小写
        cleaned = cleaned.toLowerCase();
        // 去除多余空格
        cleaned = cleaned.replaceAll("\\s+", " ").trim();
        return cleaned;
    }
    
    /**
     * 关键词提取 (简化版分词)
     */
    private List<String> extractKeywords(String text) {
        String[] words = text.split("\\s+");
        
        return Arrays.stream(words)
            .filter(w -> !STOP_WORDS.contains(w))
            .filter(w -> w.length() > 1)
            .collect(Collectors.toList());
    }
    
    /**
     * 计算原始分数
     */
    private Map<String, Integer> calculateRawScores(List<String> keywords) {
        // 初始化默认值
        Map<String, Integer> scores = new HashMap<>();
        scores.put("foodIndex", 30);
        scores.put("cultureDepth", 30);
        scores.put("natureScenery", 30);
        scores.put("leisureLevel", 50);
        scores.put("parentChild", 20);
        scores.put("photography", 40);
        scores.put("shopping", 30);
        scores.put("adventure", 20);
        
        // 遍历关键词累加分数
        for (String keyword : keywords) {
            KeywordMapper.DimensionWeight weight = KeywordMapper.KEYWORD_RULES.get(keyword);
            if (weight != null) {
                String dimension = weight.getDimension();
                int currentScore = scores.getOrDefault(dimension, 0);
                scores.put(dimension, currentScore + weight.getScore());
            }
        }
        
        return scores;
    }
    
    /**
     * 归一化处理 (转换为 0-100)
     */
    private DimensionWeights normalize(Map<String, Integer> rawScores) {
        DimensionWeights weights = new DimensionWeights();
        
        // 找出最大值
        int maxScore = rawScores.values().stream().max(Integer::compareTo).orElse(100);
        if (maxScore == 0) maxScore = 1; // 避免除以零
        
        // 归一化
        weights.setFoodIndex(normalizeValue(rawScores.get("foodIndex"), maxScore));
        weights.setCultureDepth(normalizeValue(rawScores.get("cultureDepth"), maxScore));
        weights.setNatureScenery(normalizeValue(rawScores.get("natureScenery"), maxScore));
        weights.setLeisureLevel(normalizeValue(rawScores.get("leisureLevel"), maxScore));
        weights.setParentChild(normalizeValue(rawScores.get("parentChild"), maxScore));
        weights.setPhotography(normalizeValue(rawScores.get("photography"), maxScore));
        weights.setShopping(normalizeValue(rawScores.get("shopping"), maxScore));
        weights.setAdventure(normalizeValue(rawScores.get("adventure"), maxScore));
        
        return weights;
    }
    
    /**
     * 单个维度归一化
     */
    private int normalizeValue(Integer value, int maxScore) {
        if (value == null) value = 0;
        int normalized = (value * 100) / maxScore;
        return Math.min(100, normalized);
    }
    
    /**
     * 获取默认权重
     */
    private DimensionWeights getDefaultWeights() {
        DimensionWeights weights = new DimensionWeights();
        // 所有维度默认为 50
        return weights;
    }
}
