package com.travel.itinerary.module.itinerary.service.impl;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.itinerary.common.exception.BusinessException;
import com.travel.itinerary.common.exception.ErrorCode;
import com.travel.itinerary.common.util.RedisUtil;
import com.travel.itinerary.module.ai.client.TongYiClient;
import com.travel.itinerary.module.ai.service.PromptTemplate;
import com.travel.itinerary.module.analyzer.dto.DimensionWeights;
import com.travel.itinerary.module.analyzer.service.KeywordAnalyzerService;
import com.travel.itinerary.module.itinerary.dto.*;
import com.travel.itinerary.module.itinerary.entity.Itinerary;
import com.travel.itinerary.module.itinerary.entity.ItineraryDay;
import com.travel.itinerary.module.itinerary.mapper.ItineraryDayMapper;
import com.travel.itinerary.module.itinerary.mapper.ItineraryMapper;
import com.travel.itinerary.module.itinerary.service.ItineraryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 攻略服务实现类
 * 
 * @author Travel Team
 * @version 2.0.0
 */
@Slf4j
@Service
public class ItineraryServiceImpl implements ItineraryService {
    
    @Autowired
    private ItineraryMapper itineraryMapper;
    
    @Autowired
    private ItineraryDayMapper itineraryDayMapper;
    
    @Autowired
    private KeywordAnalyzerService analyzerService;
    
    @Autowired
    private PromptTemplate promptTemplate;
    
    @Autowired
    private TongYiClient tongYiClient;
    
    @Autowired
    private RedisUtil redisUtil;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    private static final String CACHE_PREFIX = "itinerary:";
    private static final long CACHE_EXPIRE_HOURS = 24;
    
    @Override
    @Transactional
    public ItineraryVO generateAndSave(Long userId, ItineraryGenerateDTO dto) {
        log.info("开始生成攻略: 目的地={}, 天数={}", dto.getDestination(), dto.getDays());
        
        // 1. 分析用户偏好
        DimensionWeights weights = analyzerService.analyze(dto.getUserInput());
        
        // 2. 构建增强版 Prompt
        String prompt = promptTemplate.buildEnhancedPrompt(
            dto.getDestination(), 
            dto.getDays(), 
            dto.getTotalBudget(), 
            weights
        );
        
        // 3. 调用 AI 生成攻略
        String aiResponse = tongYiClient.call(prompt, 0.7, 30000);
        
        // 4. 解析 AI 响应 (JSON 格式)
        ItineraryVO vo = parseAiResponse(aiResponse);
        
        // 5. 保存到数据库
        saveItinerary(userId, dto, weights, vo);
        
        log.info("攻略生成并保存成功");
        return vo;
    }
    
    @Override
    public List<ItineraryVO> listByUserId(Long userId) {
        List<Itinerary> itineraries = itineraryMapper.findByUserId(userId);
        return itineraries.stream()
            .map(this::convertToVO)
            .collect(Collectors.toList());
    }
    
    @Override
    public ItineraryVO getDetail(Long id, Long userId) {
        // 1. 尝试从缓存获取
        String cacheKey = CACHE_PREFIX + id;
        ItineraryVO cachedVo = redisUtil.get(cacheKey);
        if (cachedVo != null) {
            log.debug("命中缓存: itineraryId={}", id);
            return cachedVo;
        }
        
        // 2. 查询数据库
        Itinerary itinerary = itineraryMapper.selectById(id);
        if (itinerary == null) {
            throw new BusinessException(ErrorCode.ITINERARY_NOT_FOUND);
        }
        
        // 权限校验
        if (!itinerary.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.ITINERARY_NO_PERMISSION);
        }
        
        ItineraryVO vo = convertToVO(itinerary);
        
        // 加载日程明细
        List<ItineraryDay> days = itineraryDayMapper.findByItineraryId(id);
        List<ItineraryDayVO> dayVOs = days.stream()
            .map(this::convertDayToVO)
            .collect(Collectors.toList());
        vo.setDailyPlans(dayVOs);
        
        // 3. 写入缓存
        redisUtil.set(cacheKey, vo, CACHE_EXPIRE_HOURS, TimeUnit.HOURS);
        
        return vo;
    }
    
    /**
     * 解析 AI 响应
     */
    private ItineraryVO parseAiResponse(String aiResponse) {
        try {
            // 清理 Markdown 代码块标记 (如果 AI 返回了 ```json ... ```)
            String jsonStr = aiResponse.trim();
            if (jsonStr.startsWith("```json")) {
                jsonStr = jsonStr.substring(7);
            }
            if (jsonStr.endsWith("```")) {
                jsonStr = jsonStr.substring(0, jsonStr.length() - 3);
            }
            jsonStr = jsonStr.trim();
            
            JsonNode root = objectMapper.readTree(jsonStr);
            
            ItineraryVO vo = new ItineraryVO();
            vo.setTitle(root.path("title").asText("未命名攻略"));
            vo.setDays(root.path("days").asInt(1));
            
            // 解析每日行程
            List<ItineraryDayVO> dailyPlans = new ArrayList<>();
            JsonNode dailyPlansNode = root.path("dailyPlans");
            if (dailyPlansNode.isArray()) {
                for (JsonNode dayNode : dailyPlansNode) {
                    ItineraryDayVO dayVO = new ItineraryDayVO();
                    dayVO.setDayNumber(dayNode.path("dayNumber").asInt(1));
                    
                    String dateStr = dayNode.path("date").asText();
                    if (dateStr != null && !dateStr.isEmpty()) {
                        dayVO.setDate(LocalDate.parse(dateStr));
                    }
                    
                    // 解析景点列表
                    List<SpotVO> spots = new ArrayList<>();
                    JsonNode spotsNode = dayNode.path("spots");
                    if (spotsNode.isArray()) {
                        for (JsonNode spotNode : spotsNode) {
                            SpotVO spot = new SpotVO();
                            spot.setTitle(spotNode.path("title").asText());
                            spot.setType(spotNode.path("type").asText("spot"));
                            spot.setStartTime(spotNode.path("startTime").asText());
                            spot.setEndTime(spotNode.path("endTime").asText());
                            spot.setLocation(spotNode.path("location").asText());
                            
                            String costStr = spotNode.path("cost").asText("0");
                            spot.setCost(new BigDecimal(costStr));
                            
                            spot.setNotes(spotNode.path("notes").asText());
                            spots.add(spot);
                        }
                    }
                    dayVO.setSpots(spots);
                    dailyPlans.add(dayVO);
                }
            }
            vo.setDailyPlans(dailyPlans);
            
            return vo;
        } catch (Exception e) {
            log.error("解析 AI 响应失败", e);
            throw new BusinessException(ErrorCode.AI_EMPTY_RESPONSE, "AI 响应格式错误");
        }
    }
    
    /**
     * 保存攻略到数据库
     */
    private void saveItinerary(Long userId, ItineraryGenerateDTO dto, DimensionWeights weights, ItineraryVO vo) {
        // 保存主表
        Itinerary itinerary = new Itinerary();
        BeanUtils.copyProperties(vo, itinerary);
        itinerary.setUserId(userId);
        itinerary.setDestination(dto.getDestination());
        itinerary.setTotalBudget(dto.getTotalBudget());
        itinerary.setStartDate(dto.getStartDate());
        itinerary.setPreferenceWeights(JSONUtil.toJsonStr(weights));
        itinerary.setStatus(1); // 已发布
        
        itineraryMapper.insert(itinerary);
        
        // 保存日程明细
        if (vo.getDailyPlans() != null) {
            for (int i = 0; i < vo.getDailyPlans().size(); i++) {
                ItineraryDayVO dayVO = vo.getDailyPlans().get(i);
                ItineraryDay day = new ItineraryDay();
                day.setItineraryId(itinerary.getId());
                day.setDayNumber(dayVO.getDayNumber());
                day.setDate(dayVO.getDate());
                day.setContent(JSONUtil.toJsonStr(dayVO.getSpots()));
                day.setSortOrder(i + 1);
                
                itineraryDayMapper.insert(day);
            }
        }
    }
    
    /**
     * 转换为 VO (不含日程明细)
     */
    private ItineraryVO convertToVO(Itinerary itinerary) {
        ItineraryVO vo = new ItineraryVO();
        BeanUtils.copyProperties(itinerary, vo);
        return vo;
    }
    
    /**
     * 转换日程为 VO
     */
    private ItineraryDayVO convertDayToVO(ItineraryDay day) {
        ItineraryDayVO vo = new ItineraryDayVO();
        vo.setDayNumber(day.getDayNumber());
        vo.setDate(day.getDate());
        vo.setDailyBudget(day.getDailyBudget());
        
        // 解析 content JSON 为 SpotVO 列表
        if (day.getContent() != null) {
            try {
                List<SpotVO> spots = JSONUtil.toList(day.getContent(), SpotVO.class);
                vo.setSpots(spots);
            } catch (Exception e) {
                log.warn("解析日程内容失败", e);
                vo.setSpots(new ArrayList<>());
            }
        }
        
        return vo;
    }
}
