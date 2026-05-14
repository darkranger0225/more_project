package com.campus.canteen.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.canteen.entity.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {
    
    List<Dish> getByWindowId(Long windowId);
    
    List<Dish> getByCategory(String category);
    
    List<Dish> getTopRated(Integer limit);
    
    List<Dish> searchByName(String name);
    
    boolean updateStatus(Long dishId, Integer status);
    
    void updateRating(Long dishId);
    
    Page<Dish> getDishPage(Integer current, Integer size, String name, Long windowId, String category);
}
