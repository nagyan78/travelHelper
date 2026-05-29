package com.travel.itinerary.module.itinerary.service;

import com.travel.itinerary.module.analyzer.dto.DimensionWeights;
import com.travel.itinerary.module.analyzer.service.KeywordAnalyzerService;
import com.travel.itinerary.module.itinerary.dto.ItineraryGenerateDTO;
import com.travel.itinerary.module.itinerary.dto.ItineraryVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 攻略服务单元测试
 * 
 * @author Travel Team
 */
@SpringBootTest
public class ItineraryServiceTest {
    
    @Autowired
    private ItineraryService itineraryService;
    
    @Autowired
    private KeywordAnalyzerService analyzerService;
    
    /**
     * 测试偏好分析逻辑
     */
    @Test
    public void testAnalyzePreference() {
        // 模拟用户输入
        String userInput = "我喜欢美食和博物馆，希望行程轻松一些";
        
        // 验证分析结果不为空
        DimensionWeights weights = analyzerService.analyze(userInput);
        Assertions.assertNotNull(weights);
        Assertions.assertTrue(weights.getFoodIndex() > 50, "美食指数应较高");
        Assertions.assertTrue(weights.getCultureDepth() > 50, "文化深度应较高");
        Assertions.assertTrue(weights.getLeisureLevel() > 50, "休闲程度应较高");
    }
    
    /**
     * 测试攻略生成流程
     */
    @Test
    public void testGenerateItinerary() {
        Long userId = 1L;
        ItineraryGenerateDTO dto = new ItineraryGenerateDTO();
        dto.setDestination("杭州");
        dto.setDays(2);
        dto.setTotalBudget(new BigDecimal("2000"));
        dto.setStartDate(LocalDate.now().plusDays(1));
        dto.setUserInput("喜欢自然风光和拍照");
        
        ItineraryVO vo = itineraryService.generateAndSave(userId, dto);
        
        Assertions.assertNotNull(vo);
        Assertions.assertEquals("杭州", vo.getDestination());
        Assertions.assertNotNull(vo.getDailyPlans());
        Assertions.assertFalse(vo.getDailyPlans().isEmpty());
    }
}
