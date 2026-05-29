package com.travel.itinerary.module.ai.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.itinerary.common.exception.BusinessException;
import com.travel.itinerary.common.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * 通义千问 API 客户端
 * 
 * @author Travel Team
 * @version 2.0.0
 */
@Slf4j
@Component
public class TongYiClient {
    
    @Value("${aliyun.tongyi.api-key}")
    private String apiKey;
    
    @Value("${aliyun.tongyi.api-url:https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions}")
    private String apiUrl;
    
    @Value("${aliyun.tongyi.model:qwen-max}")
    private String model;
    
    @Value("${aliyun.tongyi.timeout:30000}")
    private Integer timeout;
    
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * 调用通义千问 API
     * 
     * @param prompt 提示词
     * @param temperature 温度参数 (0.0-1.0)
     * @param timeout 超时时间 (毫秒)
     * @return AI 生成的文本
     */
    public String call(String prompt, Double temperature, Integer timeout) {
        log.info("调用通义千问API, prompt长度: {}", prompt.length());
        
        int maxRetries = 3;
        for (int retryCount = 1; retryCount <= maxRetries; retryCount++) {
            try {
                // 构建请求头
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.set("Authorization", "Bearer " + apiKey);
                
                // 构建请求体
                Map<String, Object> requestBody = new HashMap<>();
                requestBody.put("model", model);
                
                Map<String, Object> input = new HashMap<>();
                input.put("messages", new Object[]{
                    Map.of("role", "user", "content", prompt)
                });
                requestBody.put("input", input);
                
                Map<String, Object> parameters = new HashMap<>();
                parameters.put("temperature", temperature != null ? temperature : 0.7);
                parameters.put("max_tokens", 4000);
                parameters.put("result_format", "message");
                requestBody.put("parameters", parameters);
                
                HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
                
                // 发送请求
                ResponseEntity<String> response = restTemplate.exchange(
                    apiUrl, HttpMethod.POST, entity, String.class
                );
                
                // 解析响应
                String responseBody = response.getBody();
                if (responseBody == null) {
                    throw new BusinessException(ErrorCode.AI_EMPTY_RESPONSE);
                }
                
                JsonNode root = objectMapper.readTree(responseBody);
                JsonNode output = root.path("output");
                String text = output.path("choices").get(0).path("message").path("content").asText();
                
                if (text == null || text.isEmpty()) {
                    throw new BusinessException(ErrorCode.AI_EMPTY_RESPONSE);
                }
                
                log.debug("AI响应成功");
                return text;
                
            } catch (BusinessException e) {
                throw e;
            } catch (Exception e) {
                log.warn("第{}次调用失败: {}", retryCount, e.getMessage());
                if (retryCount == maxRetries) {
                    log.error("通义千问 API 调用最终失败", e);
                    throw new BusinessException(ErrorCode.AI_SERVICE_ERROR, "AI 服务调用失败: " + e.getMessage());
                }
                try {
                    Thread.sleep(1000); // 等待 1 秒后重试
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        throw new BusinessException(ErrorCode.AI_SERVICE_ERROR);
    }
}
