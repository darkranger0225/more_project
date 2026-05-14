package com.campus.canteen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.canteen.entity.Review;
import com.campus.canteen.mapper.ReviewMapper;
import com.campus.canteen.service.DishService;
import com.campus.canteen.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReviewServiceImpl extends ServiceImpl<ReviewMapper, Review> implements ReviewService {
    
    @Autowired
    private DishService dishService;
    
    @Override
    public List<Review> getByDishId(Long dishId) {
        return baseMapper.selectByDishId(dishId);
    }
    
    @Override
    public List<Review> getByUserId(Long userId) {
        return baseMapper.selectByUserId(userId);
    }
    
    @Override
    @Transactional
    public boolean addReview(Review review) {
        review.setStatus(Review.STATUS_APPROVED);
        review.setLikeCount(0);
        boolean success = save(review);
        if (success) {
            dishService.updateRating(review.getDishId());
        }
        return success;
    }
    
    @Override
    public boolean likeReview(Long reviewId) {
        baseMapper.incrementLikeCount(reviewId);
        return true;
    }
    
    @Override
    @Transactional
    public boolean auditReview(Long reviewId, Integer status) {
        Review review = getById(reviewId);
        if (review == null) {
            return false;
        }
        review.setStatus(status);
        boolean success = updateById(review);
        if (success) {
            dishService.updateRating(review.getDishId());
        }
        return success;
    }
    
    @Override
    public List<Review> getPendingReviews() {
        return baseMapper.selectPendingReviews();
    }
    
    @Override
    public Page<Review> getReviewPage(Integer current, Integer size, Long dishId, Long userId, Integer status) {
        Page<Review> page = new Page<>(current, size);
        LambdaQueryWrapper<Review> wrapper = new LambdaQueryWrapper<>();
        
        if (dishId != null) {
            wrapper.eq(Review::getDishId, dishId);
        }
        if (userId != null) {
            wrapper.eq(Review::getUserId, userId);
        }
        if (status != null) {
            wrapper.eq(Review::getStatus, status);
        }
        
        wrapper.eq(Review::getDeleted, 0);
        wrapper.orderByDesc(Review::getCreateTime);
        
        return page(page, wrapper);
    }
    
    @Override
    public List<Review> getReviewsWithDetails() {
        return baseMapper.selectWithDetails();
    }
}
