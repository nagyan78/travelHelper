package com.travel.itinerary.module.itinerary.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.travel.itinerary.module.itinerary.entity.Itinerary;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 攻略数据访问接口
 * 
 * @author Travel Team
 * @version 2.0.0
 */
@Mapper
public interface ItineraryMapper extends BaseMapper<Itinerary> {
    
    /**
     * 查询用户的攻略列表
     * 
     * @param userId 用户ID
     * @return 攻略列表
     */
    default List<Itinerary> findByUserId(Long userId) {
        return selectList(
            new LambdaQueryWrapper<Itinerary>()
                .eq(Itinerary::getUserId, userId)
                .orderByDesc(Itinerary::getCreateTime)
        );
    }
}
