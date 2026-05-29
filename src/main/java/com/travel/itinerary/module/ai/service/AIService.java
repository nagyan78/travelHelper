package com.travel.itinerary.module.ai.service;

import com.travel.itinerary.module.ai.client.TongYiClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * AI 服务封装类
 * 提供统一的 AI 调用接口
 * 
 * @author Travel Team
 * @version 2.0.0
 */
@Slf4j
@Service
public class AIService {
    
    @Autowired
    private TongYiClient tongYiClient;
    
    /**
     * 聊天对话
     * 
     * @param prompt 提示词
     * @return AI 回答
     */
    public String chat(String prompt) {
        log.debug("调用AI聊天, prompt长度: {}", prompt.length());
        return tongYiClient.call(prompt, 0.7, 30000);
    }
    
    /**
     * 聊天对话 (自定义温度参数)
     * 
     * @param prompt 提示词
     * @param temperature 温度参数 (0.0-1.0)
     * @return AI 回答
     */
    public String chat(String prompt, Double temperature) {
        log.debug("调用AI聊天, temperature={}", temperature);
        return tongYiClient.call(prompt, temperature, 30000);
    }
}