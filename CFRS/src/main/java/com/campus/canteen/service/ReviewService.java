package com.campus.canteen.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.canteen.entity.Review;

import java.util.List;

public interface ReviewService extends IService<Review> {
    
    List<Review> getByDishId(Long dishId);
    
    List<Review> getByUserId(Long userId);
    
    boolean addReview(Review review);
    
    boolean likeReview(Long reviewId);
    
    boolean auditReview(Long reviewId, Integer status);
    
    List<Review> getPendingReviews();
    
    Page<Review> getReviewPage(Integer current, Integer size, Long dishId, Long userId, Integer status);
    
    List<Review> getReviewsWithDetails();
}
