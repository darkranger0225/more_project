package com.campus.canteen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campus.canteen.entity.Dish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
    
    @Select("SELECT * FROM dish WHERE window_id = #{windowId} AND deleted = 0 ORDER BY create_time DESC")
    List<Dish> selectByWindowId(Long windowId);
    
    @Select("SELECT * FROM dish WHERE category = #{category} AND status = 1 AND deleted = 0 ORDER BY average_rating DESC")
    List<Dish> selectByCategory(String category);
    
    @Select("SELECT * FROM dish WHERE status = 1 AND deleted = 0 ORDER BY average_rating DESC LIMIT #{limit}")
    List<Dish> selectTopRated(Integer limit);
    
    @Select("SELECT * FROM dish WHERE name LIKE CONCAT('%', #{name}, '%') AND deleted = 0")
    List<Dish> searchByName(String name);
    
    @Update("UPDATE dish SET average_rating = #{averageRating}, review_count = #{reviewCount} WHERE id = #{dishId}")
    void updateRating(@Param("dishId") Long dishId, @Param("averageRating") Double averageRating, @Param("reviewCount") Integer reviewCount);
}
