package com.campus.canteen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.canteen.entity.DishTag;
import com.campus.canteen.entity.Tag;
import com.campus.canteen.mapper.DishTagMapper;
import com.campus.canteen.mapper.TagMapper;
import com.campus.canteen.service.TagService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Resource
    private DishTagMapper dishTagMapper;

    @Override
    public List<Tag> getAllTags() {
        return list(new QueryWrapper<Tag>().eq("deleted", 0));
    }

    @Override
    public List<Tag> getTagsByDishId(Long dishId) {
        List<DishTag> dishTags = dishTagMapper.selectList(new QueryWrapper<DishTag>().eq("dish_id", dishId));
        List<Long> tagIds = dishTags.stream().map(DishTag::getTagId).collect(Collectors.toList());
        if (tagIds.isEmpty()) {
            return List.of();
        }
        return list(new QueryWrapper<Tag>().in("id", tagIds).eq("deleted", 0));
    }

    @Override
    public boolean updateDishTags(Long dishId, List<Long> tagIds) {
        // 删除该菜品的所有现有标签关联
        QueryWrapper<DishTag> wrapper = new QueryWrapper<>();
        wrapper.eq("dish_id", dishId);
        dishTagMapper.delete(wrapper);
        
        // 添加新的标签关联
        if (tagIds != null && !tagIds.isEmpty()) {
            for (Long tagId : tagIds) {
                DishTag dishTag = new DishTag();
                dishTag.setDishId(dishId);
                dishTag.setTagId(tagId);
                dishTagMapper.insert(dishTag);
            }
        }
        return true;
    }
}