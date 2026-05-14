package com.campus.canteen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.canteen.entity.Tag;

import java.util.List;

public interface TagService extends IService<Tag> {
    List<Tag> getTagsByDishId(Long dishId);
    List<Tag> getAllTags();
    boolean updateDishTags(Long dishId, List<Long> tagIds);
}