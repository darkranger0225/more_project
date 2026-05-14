package com.campus.canteen.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.canteen.common.PageResult;
import com.campus.canteen.common.Result;
import com.campus.canteen.entity.Dish;
import com.campus.canteen.entity.Tag;
import com.campus.canteen.service.DishService;
import com.campus.canteen.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@Controller
public class DishController {
    
    @Autowired
    private DishService dishService;
    
    @Autowired
    private TagService tagService;
    
    @GetMapping("/dishes")
    public String dishesPage(@RequestParam(required = false) Long windowId,
                              @RequestParam(required = false) String category,
                              Model model) {
        model.addAttribute("windowId", windowId);
        model.addAttribute("category", category);
        return "dishes";
    }
    
    @GetMapping("/dish/{id}")
    public String dishDetailPage(@PathVariable Long id, Model model) {
        Dish dish = dishService.getById(id);
        if (dish == null) {
            return "redirect:/dishes";
        }
        List<Tag> tags = tagService.getTagsByDishId(id);
        model.addAttribute("dish", dish);
        model.addAttribute("tags", tags);
        return "dish-detail";
    }
    
    @GetMapping("/api/dishes")
    @ResponseBody
    public Result<PageResult<Dish>> getDishes(@RequestParam(defaultValue = "1") Integer current,
                                               @RequestParam(defaultValue = "10") Integer size,
                                               @RequestParam(required = false) String name,
                                               @RequestParam(required = false) Long windowId,
                                               @RequestParam(required = false) String category) {
        Page<Dish> page = dishService.getDishPage(current, size, name, windowId, category);
        PageResult<Dish> pageResult = new PageResult<>(page.getTotal(), (long) current, (long) size, page.getRecords());
        return Result.success(pageResult);
    }
    
    @GetMapping("/api/dishes/{id}")
    @ResponseBody
    public Result<Dish> getDishById(@PathVariable Long id) {
        Dish dish = dishService.getById(id);
        if (dish == null) {
            return Result.error("菜品不存在");
        }
        return Result.success(dish);
    }
    
    @GetMapping("/api/dishes/window/{windowId}")
    @ResponseBody
    public Result<List<Dish>> getDishesByWindow(@PathVariable Long windowId) {
        List<Dish> dishes = dishService.getByWindowId(windowId);
        return Result.success(dishes);
    }
    
    @GetMapping("/api/dishes/category/{category}")
    @ResponseBody
    public Result<List<Dish>> getDishesByCategory(@PathVariable String category) {
        List<Dish> dishes = dishService.getByCategory(category);
        return Result.success(dishes);
    }
    
    @GetMapping("/api/dishes/top-rated")
    @ResponseBody
    public Result<List<Dish>> getTopRatedDishes(@RequestParam(defaultValue = "10") Integer limit) {
        List<Dish> dishes = dishService.getTopRated(limit);
        return Result.success(dishes);
    }
    
    @GetMapping("/api/dishes/search")
    @ResponseBody
    public Result<List<Dish>> searchDishes(@RequestParam String name) {
        List<Dish> dishes = dishService.searchByName(name);
        return Result.success(dishes);
    }
    
    @PostMapping("/api/admin/dishes")
    @ResponseBody
    public Result<String> addDish(@RequestParam("name") String name,
                                 @RequestParam("windowId") Long windowId,
                                 @RequestParam("category") String category,
                                 @RequestParam("price") String price,
                                 @RequestParam("description") String description,
                                 @RequestParam(value = "image", required = false) MultipartFile image) {
        Dish dish = new Dish();
        dish.setName(name);
        dish.setWindowId(windowId);
        dish.setCategory(category);
        dish.setPrice(new java.math.BigDecimal(price));
        dish.setDescription(description);
        dish.setAverageRating(0.0);
        dish.setReviewCount(0);
        dish.setStatus(Dish.STATUS_ON_SALE);
        
        if (image != null && !image.isEmpty()) {
            String imagePath = saveImage(image);
            if (imagePath != null) {
                dish.setImage(imagePath);
            } else {
                return Result.error("图片上传失败");
            }
        }
        
        boolean success = dishService.save(dish);
        if (success) {
            return Result.success("添加成功");
        }
        return Result.error("添加失败");
    }
    
    @PutMapping("/api/admin/dishes/{id}")
    @ResponseBody
    public Result<String> updateDish(@PathVariable Long id,
                                    @RequestParam("name") String name,
                                    @RequestParam("windowId") Long windowId,
                                    @RequestParam("category") String category,
                                    @RequestParam("price") String price,
                                    @RequestParam("description") String description,
                                    @RequestParam(value = "image", required = false) MultipartFile image) {
        Dish dish = dishService.getById(id);
        if (dish == null) {
            return Result.error("菜品不存在");
        }
        
        dish.setName(name);
        dish.setWindowId(windowId);
        dish.setCategory(category);
        dish.setPrice(new java.math.BigDecimal(price));
        dish.setDescription(description);
        
        if (image != null && !image.isEmpty()) {
            String imagePath = saveImage(image);
            if (imagePath != null) {
                dish.setImage(imagePath);
            } else {
                return Result.error("图片上传失败");
            }
        }
        
        boolean success = dishService.updateById(dish);
        if (success) {
            return Result.success("更新成功");
        }
        return Result.error("更新失败");
    }
    
    private String saveImage(MultipartFile image) {
        try {
            // 获取项目根目录的绝对路径
            String projectPath = System.getProperty("user.dir");
            String uploadDir = projectPath + "/src/main/resources/static/images/";
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            
            String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            String filePath = uploadDir + fileName;
            File dest = new File(filePath);
            image.transferTo(dest);
            
            // 返回相对路径，用于数据库存储和前端访问
            return "/images/" + fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @DeleteMapping("/api/admin/dishes/{id}")
    @ResponseBody
    public Result<String> deleteDish(@PathVariable Long id) {
        boolean success = dishService.removeById(id);
        if (success) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }
    
    @PostMapping("/api/admin/dishes/{id}/status")
    @ResponseBody
    public Result<String> updateDishStatus(@PathVariable Long id, @RequestParam Integer status) {
        boolean success = dishService.updateStatus(id, status);
        if (success) {
            return Result.success("状态更新成功");
        }
        return Result.error("状态更新失败");
    }
    
    @PostMapping("/api/admin/dishes/{id}/tags")
    @ResponseBody
    public Result<String> updateDishTags(@PathVariable Long id, @RequestBody List<Long> tagIds) {
        boolean success = tagService.updateDishTags(id, tagIds);
        if (success) {
            return Result.success("标签更新成功");
        }
        return Result.error("标签更新失败");
    }
}
