package com.travel.itinerary.module.assistant.controller;

import com.travel.itinerary.common.result.Result;
import com.travel.itinerary.common.util.JwtUtil;
import com.travel.itinerary.module.assistant.service.TravelAssistantService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * AI旅行助手控制器
 * 
 * @author Travel Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/assistant")
public class TravelAssistantController {
    
    @Autowired
    private TravelAssistantService assistantService;
    
    /**
     * 提问
     */
    @PostMapping("/ask")
    public Result<String> ask(@RequestBody AskRequest request) {
        Long userId = JwtUtil.getCurrentUserId();
        String answer = assistantService.askQuestion(
            userId, 
            request.getQuestion(),
            request.getItineraryId()
        );
        return Result.success(answer);
    }
    
    /**
     * 推荐周边景点
     */
    @GetMapping("/recommend")
    public Result<List<String>> recommend(
        @RequestParam double lat,
        @RequestParam double lng,
        @RequestParam(required = false) String preference
    ) {
        return Result.success(
            assistantService.recommendNearbySpots(lat, lng, preference)
        );
    }
    
    /**
     * 行程优化建议
     */
    @GetMapping("/optimize/{itineraryId}")
    public Result<List<String>> optimize(@PathVariable Long itineraryId) {
        return Result.success(assistantService.optimizeItinerary(itineraryId));
    }
    
    /**
     * 常见问题
     */
    @GetMapping("/quick/{category}")
    public Result<String> quickAnswer(@PathVariable String category) {
        return Result.success(assistantService.getQuickAnswer(category));
    }
    
    /**
     * 提问请求
     */
    @Data
    public static class AskRequest {
        private String question;      // 问题内容
        private Long itineraryId;     // 关联的攻略ID (可选)
    }
}
