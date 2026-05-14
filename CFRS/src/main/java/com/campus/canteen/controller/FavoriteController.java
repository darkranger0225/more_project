package com.campus.canteen.controller;

import com.campus.canteen.common.Result;
import com.campus.canteen.entity.User;
import com.campus.canteen.service.FavoriteService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/favorite")
public class FavoriteController {

    @Resource
    private FavoriteService favoriteService;

    @PostMapping("/add")
    @ResponseBody
    public Result addFavorite(@RequestParam Integer targetType, @RequestParam Long targetId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.error(401, "请先登录");
        }
        Long userId = user.getId();
        boolean result = favoriteService.addFavorite(userId, targetType, targetId);
        return result ? Result.success("收藏成功") : Result.error("收藏失败");
    }

    @PostMapping("/remove")
    @ResponseBody
    public Result removeFavorite(@RequestParam Integer targetType, @RequestParam Long targetId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.error(401, "请先登录");
        }
        Long userId = user.getId();
        boolean result = favoriteService.removeFavorite(userId, targetType, targetId);
        return result ? Result.success("取消收藏成功") : Result.error("取消收藏失败");
    }

    @GetMapping("/check")
    @ResponseBody
    public Result checkFavorite(@RequestParam Integer targetType, @RequestParam Long targetId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.success(false);
        }
        Long userId = user.getId();
        boolean isFavorite = favoriteService.isFavorite(userId, targetType, targetId);
        return Result.success(isFavorite);
    }

    @GetMapping("/list")
    @ResponseBody
    public Result getFavorites(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.error("请先登录");
        }
        Long userId = user.getId();
        return Result.success(favoriteService.getFavoritesByUserId(userId));
    }
}
