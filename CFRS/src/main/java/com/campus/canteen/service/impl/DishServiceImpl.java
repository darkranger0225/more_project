package com.campus.canteen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.canteen.entity.Dish;
import com.campus.canteen.mapper.DishMapper;
import com.campus.canteen.mapper.ReviewMapper;
import com.campus.canteen.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    
    @Autowired
    private ReviewMapper reviewMapper;
    
    @Override
    public List<Dish> getByWindowId(Long windowId) {
        return baseMapper.selectByWindowId(windowId);
    }
    
    @Override
    public List<Dish> getByCategory(String category) {
        return baseMapper.selectByCategory(category);
    }
    
    @Override
    public List<Dish> getTopRated(Integer limit) {
        return baseMapper.selectTopRated(limit);
    }
    
    @Override
    public List<Dish> searchByName(String name) {
        return baseMapper.searchByName(name);
    }
    
    @Override
    public boolean updateStatus(Long dishId, Integer status) {
        Dish dish = getById(dishId);
        if (dish == null) {
            return false;
        }
        dish.setStatus(status);
        return updateById(dish);
    }
    
    @Override
    public void updateRating(Long dishId) {
        Double averageRating = reviewMapper.selectAverageRatingByDishId(dishId);
        Integer reviewCount = reviewMapper.selectReviewCountByDishId(dishId);
        if (averageRating == null) {
            averageRating = 0.0;
        }
        if (reviewCount == null) {
            reviewCount = 0;
        }
        baseMapper.updateRating(dishId, averageRating, reviewCount);
    }
    
    @Override
    public Page<Dish> getDishPage(Integer current, Integer size, String name, Long windowId, String category) {
        Page<Dish> page = new Page<>(current, size);
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(name)) {
            wrapper.like(Dish::getName, name);
        }
        if (windowId != null) {
            wrapper.eq(Dish::getWindowId, windowId);
        }
        if (StringUtils.hasText(category)) {
            wrapper.eq(Dish::getCategory, category);
        }
        
        wrapper.eq(Dish::getDeleted, 0);
        wrapper.orderByDesc(Dish::getCreateTime);
        
        return page(page, wrapper);
    }
}
