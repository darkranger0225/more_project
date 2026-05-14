package com.campus.canteen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.canteen.entity.Favorite;
import com.campus.canteen.mapper.FavoriteMapper;
import com.campus.canteen.service.FavoriteService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteServiceImpl extends ServiceImpl<FavoriteMapper, Favorite> implements FavoriteService {

    @Override
    public boolean addFavorite(Long userId, Integer targetType, Long targetId) {
        // 检查是否已经收藏
        if (isFavorite(userId, targetType, targetId)) {
            return true;
        }
        Favorite favorite = new Favorite();
        favorite.setUserId(userId);
        favorite.setTargetType(targetType);
        favorite.setTargetId(targetId);
        return save(favorite);
    }

    @Override
    public boolean removeFavorite(Long userId, Integer targetType, Long targetId) {
        QueryWrapper<Favorite> wrapper = new QueryWrapper<Favorite>()
                .eq("user_id", userId)
                .eq("target_type", targetType)
                .eq("target_id", targetId)
                .eq("deleted", 0);
        return remove(wrapper);
    }

    @Override
    public boolean isFavorite(Long userId, Integer targetType, Long targetId) {
        QueryWrapper<Favorite> wrapper = new QueryWrapper<Favorite>()
                .eq("user_id", userId)
                .eq("target_type", targetType)
                .eq("target_id", targetId)
                .eq("deleted", 0);
        return count(wrapper) > 0;
    }

    @Override
    public List<Favorite> getFavoritesByUserId(Long userId) {
        return list(new QueryWrapper<Favorite>()
                .eq("user_id", userId)
                .eq("deleted", 0)
                .orderByDesc("create_time"));
    }
}