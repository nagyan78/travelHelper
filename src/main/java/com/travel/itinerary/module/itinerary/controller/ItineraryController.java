package com.travel.itinerary.module.itinerary.controller;

import com.travel.itinerary.common.result.Result;
import com.travel.itinerary.module.itinerary.dto.ItineraryGenerateDTO;
import com.travel.itinerary.module.itinerary.dto.ItineraryVO;
import com.travel.itinerary.module.itinerary.service.ItineraryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 攻略管理控制器
 * 
 * @author Travel Team
 * @version 2.0.0
 */
@Slf4j
@RestController
@RequestMapping("/api/itinerary")
public class ItineraryController {
    
    @Autowired
    private ItineraryService itineraryService;
    
    /**
     * 生成并保存攻略
     */
    @PostMapping("/generate")
    public Result<ItineraryVO> generate(@Validated @RequestBody ItineraryGenerateDTO dto, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        ItineraryVO vo = itineraryService.generateAndSave(userId, dto);
        return Result.success(vo);
    }
    
    /**
     * 查询用户的攻略列表
     */
    @GetMapping("/list")
    public Result<List<ItineraryVO>> list(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        List<ItineraryVO> list = itineraryService.listByUserId(userId);
        return Result.success(list);
    }
    
    /**
     * 查询攻略详情
     */
    @GetMapping("/{id}")
    public Result<ItineraryVO> detail(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        ItineraryVO vo = itineraryService.getDetail(id, userId);
        return Result.success(vo);
    }
}