package com.campus.canteen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campus.canteen.entity.Review;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ReviewMapper extends BaseMapper<Review> {
    
    @Select("SELECT r.*, u.username, u.avatar as user_avatar FROM review r " +
            "LEFT JOIN user u ON r.user_id = u.id " +
            "WHERE r.dish_id = #{dishId} AND r.status = 1 AND r.deleted = 0 " +
            "ORDER BY r.create_time DESC")
    List<Review> selectByDishId(Long dishId);
    
    @Select("SELECT r.*, u.username, u.avatar as user_avatar, d.name as dish_name FROM review r " +
            "LEFT JOIN user u ON r.user_id = u.id " +
            "LEFT JOIN dish d ON r.dish_id = d.id " +
            "WHERE r.user_id = #{userId} AND r.deleted = 0 " +
            "ORDER BY r.create_time DESC")
    List<Review> selectByUserId(Long userId);
    
    @Select("SELECT AVG(rating) FROM review WHERE dish_id = #{dishId} AND status = 1 AND deleted = 0")
    Double selectAverageRatingByDishId(Long dishId);
    
    @Select("SELECT COUNT(*) FROM review WHERE dish_id = #{dishId} AND status = 1 AND deleted = 0")
    Integer selectReviewCountByDishId(Long dishId);
    
    @Update("UPDATE review SET like_count = like_count + 1 WHERE id = #{reviewId}")
    void incrementLikeCount(Long reviewId);
    
    @Select("SELECT r.*, u.username, u.avatar as user_avatar FROM review r " +
            "LEFT JOIN user u ON r.user_id = u.id " +
            "WHERE r.status = 0 AND r.deleted = 0 " +
            "ORDER BY r.create_time DESC")
    List<Review> selectPendingReviews();
    
    @Select("SELECT r.*, u.username, u.avatar as user_avatar, d.name as dish_name FROM review r " +
            "LEFT JOIN user u ON r.user_id = u.id " +
            "LEFT JOIN dish d ON r.dish_id = d.id " +
            "WHERE r.deleted = 0 " +
            "ORDER BY r.create_time DESC")
    List<Review> selectWithDetails();
}
