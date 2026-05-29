package com.travel.itinerary.module.analyzer.controller;

import com.travel.itinerary.common.result.Result;
import com.travel.itinerary.module.analyzer.dto.AnalyzeRequest;
import com.travel.itinerary.module.analyzer.dto.DimensionWeights;
import com.travel.itinerary.module.analyzer.service.KeywordAnalyzerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 智能偏好分析控制器
 * 
 * @author Travel Team
 * @version 2.0.0
 */
@Slf4j
@RestController
@RequestMapping("/api/analyzer")
public class KeywordAnalyzerController {
    
    @Autowired
    private KeywordAnalyzerService analyzerService;
    
    /**
     * 分析旅行偏好
     */
    @PostMapping("/analyze")
    public Result<DimensionWeights> analyze(@Validated @RequestBody AnalyzeRequest request) {
        DimensionWeights weights = analyzerService.analyze(request.getUserInput());
        return Result.success(weights);
    }
}