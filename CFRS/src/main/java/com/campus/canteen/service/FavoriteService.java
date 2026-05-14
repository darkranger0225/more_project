package com.campus.canteen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.canteen.entity.Favorite;

import java.util.List;

public interface FavoriteService extends IService<Favorite> {
    boolean addFavorite(Long userId, Integer targetType, Long targetId);
    boolean removeFavorite(Long userId, Integer targetType, Long targetId);
    boolean isFavorite(Long userId, Integer targetType, Long targetId);
    List<Favorite> getFavoritesByUserId(Long userId);
}