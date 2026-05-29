package com.travel.itinerary.module.itinerary.service;

import com.travel.itinerary.module.itinerary.dto.ItineraryGenerateDTO;
import com.travel.itinerary.module.itinerary.dto.ItineraryVO;

import java.util.List;

/**
 * 攻略服务接口
 * 
 * @author Travel Team
 * @version 2.0.0
 */
public interface ItineraryService {
    
    /**
     * 生成并保存攻略
     * 
     * @param userId 用户ID
     * @param dto 生成请求
     * @return 生成的攻略信息
     */
    ItineraryVO generateAndSave(Long userId, ItineraryGenerateDTO dto);
    
    /**
     * 查询用户的攻略列表
     * 
     * @param userId 用户ID
     * @return 攻略列表
     */
    List<ItineraryVO> listByUserId(Long userId);
    
    /**
     * 查询攻略详情
     * 
     * @param id 攻略ID
     * @param userId 用户ID (用于权限校验)
     * @return 攻略详情
     */
    ItineraryVO getDetail(Long id, Long userId);
}
