package com.campus.canteen.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.canteen.common.PageResult;
import com.campus.canteen.common.Result;
import com.campus.canteen.entity.Review;
import com.campus.canteen.entity.User;
import com.campus.canteen.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ReviewController {
    
    @Autowired
    private ReviewService reviewService;
    
    @GetMapping("/api/reviews")
    @ResponseBody
    public Result<PageResult<Review>> getReviews(@RequestParam(defaultValue = "1") Integer current,
                                                  @RequestParam(defaultValue = "10") Integer size,
                                                  @RequestParam(required = false) Long dishId,
                                                  @RequestParam(required = false) Long userId,
                                                  @RequestParam(required = false) Integer status) {
        Page<Review> page = reviewService.getReviewPage(current, size, dishId, userId, status);
        PageResult<Review> pageResult = new PageResult<>(page.getTotal(), (long) current, (long) size, page.getRecords());
        return Result.success(pageResult);
    }
    
    @GetMapping("/api/reviews/dish/{dishId}")
    @ResponseBody
    public Result<List<Review>> getReviewsByDish(@PathVariable Long dishId) {
        List<Review> reviews = reviewService.getByDishId(dishId);
        return Result.success(reviews);
    }
    
    @GetMapping("/api/reviews/user/{userId}")
    @ResponseBody
    public Result<List<Review>> getReviewsByUser(@PathVariable Long userId) {
        List<Review> reviews = reviewService.getByUserId(userId);
        return Result.success(reviews);
    }
    
    @GetMapping("/api/reviews/{id}")
    @ResponseBody
    public Result<Review> getReviewById(@PathVariable Long id) {
        Review review = reviewService.getById(id);
        if (review == null) {
            return Result.error("评价不存在");
        }
        return Result.success(review);
    }
    
    @PostMapping("/api/reviews")
    @ResponseBody
    public Result<String> addReview(@RequestBody Review review, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.error(401, "请先登录");
        }
        
        if (review.getRating() == null || review.getRating() < 0.5 || review.getRating() > 5) {
            return Result.error("评分必须在0.5-5之间");
        }
        
        if (review.getContent() != null && review.getContent().length() > 200) {
            return Result.error("评价内容不能超过200字");
        }
        
        review.setUserId(user.getId());
        boolean success = reviewService.addReview(review);
        if (success) {
            return Result.success("评价成功");
        }
        return Result.error("评价失败");
    }
    
    @DeleteMapping("/api/reviews/{id}")
    @ResponseBody
    public Result<String> deleteReview(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.error(401, "请先登录");
        }
        
        Review review = reviewService.getById(id);
        if (review == null) {
            return Result.error("评价不存在");
        }
        
        if (!review.getUserId().equals(user.getId()) && user.getRole() != User.ROLE_ADMIN) {
            return Result.error(403, "无权删除此评价");
        }
        
        boolean success = reviewService.removeById(id);
        if (success) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }
    
    @PostMapping("/api/reviews/{id}/like")
    @ResponseBody
    public Result<String> likeReview(@PathVariable Long id) {
        boolean success = reviewService.likeReview(id);
        if (success) {
            return Result.success("点赞成功");
        }
        return Result.error("点赞失败");
    }
    
    @GetMapping("/api/admin/reviews/pending")
    @ResponseBody
    public Result<List<Review>> getPendingReviews() {
        List<Review> reviews = reviewService.getPendingReviews();
        return Result.success(reviews);
    }
    
    @PostMapping("/api/admin/reviews/{id}/audit")
    @ResponseBody
    public Result<String> auditReview(@PathVariable Long id, @RequestParam Integer status) {
        boolean success = reviewService.auditReview(id, status);
        if (success) {
            return Result.success("审核完成");
        }
        return Result.error("审核失败");
    }
    
    @GetMapping("/api/reviews/details")
    @ResponseBody
    public Result<List<Review>> getReviewsWithDetails() {
        List<Review> reviews = reviewService.getReviewsWithDetails();
        return Result.success(reviews);
    }
}
