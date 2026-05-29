package com.travel.itinerary.module.itinerary.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.travel.itinerary.module.itinerary.entity.ItineraryDay;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 攻略日程数据访问接口
 * 
 * @author Travel Team
 * @version 2.0.0
 */
@Mapper
public interface ItineraryDayMapper extends BaseMapper<ItineraryDay> {
    
    /**
     * 根据攻略ID查询日程列表
     * 
     * @param itineraryId 攻略ID
     * @return 日程列表
     */
    default List<ItineraryDay> findByItineraryId(Long itineraryId) {
        return selectList(
            new LambdaQueryWrapper<ItineraryDay>()
                .eq(ItineraryDay::getItineraryId, itineraryId)
                .orderByAsc(ItineraryDay::getDayNumber)
        );
    }
}
