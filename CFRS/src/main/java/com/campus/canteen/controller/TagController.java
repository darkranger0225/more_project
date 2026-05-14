package com.campus.canteen.controller;

import com.campus.canteen.common.Result;
import com.campus.canteen.entity.Tag;
import com.campus.canteen.service.TagService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Controller
@RequestMapping("/tag")
public class TagController {

    @Resource
    private TagService tagService;

    @GetMapping("/all")
    @ResponseBody
    public Result getAllTags() {
        return Result.success(tagService.getAllTags());
    }

    @GetMapping("/dish/{id}")
    @ResponseBody
    public Result getTagsByDishId(@PathVariable Long id) {
        return Result.success(tagService.getTagsByDishId(id));
    }
    
    @PostMapping("/add")
    @ResponseBody
    public Result addTag(@RequestBody Tag tag) {
        boolean success = tagService.save(tag);
        return success ? Result.success("添加标签成功") : Result.error("添加标签失败");
    }
    
    @PostMapping("/update")
    @ResponseBody
    public Result updateTag(@RequestBody Tag tag) {
        return tagService.updateById(tag) ? Result.success("更新标签成功") : Result.error("更新标签失败");
    }
    
    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public Result deleteTag(@PathVariable Long id) {
        return tagService.removeById(id) ? Result.success("删除标签成功") : Result.error("删除标签失败");
    }
    
    @GetMapping("/admin")
    public String tagManagement() {
        return "admin/tag-management";
    }
}
