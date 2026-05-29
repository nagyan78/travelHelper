package com.travel.itinerary.module.user.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.travel.itinerary.module.user.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户数据访问接口
 * 
 * @author Travel Team
 * @version 2.0.0
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    
    /**
     * 判断用户名是否存在
     * 
     * @param username 用户名
     * @return true-存在，false-不存在
     */
    default boolean existsByUsername(String username) {
        return selectCount(
            new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username)
        ) > 0;
    }
    
    /**
     * 根据用户名查询用户
     * 
     * @param username 用户名
     * @return 用户对象
     */
    default User findByUsername(String username) {
        return selectOne(
            new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username)
        );
    }
}
