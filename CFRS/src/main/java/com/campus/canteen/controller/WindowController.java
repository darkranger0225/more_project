package com.campus.canteen.controller;

import com.campus.canteen.common.Result;
import com.campus.canteen.entity.Window;
import com.campus.canteen.service.WindowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class WindowController {
    
    @Autowired
    private WindowService windowService;
    
    @GetMapping("/windows")
    public String windowsPage(Model model) {
        List<Window> firstFloorWindows = windowService.getByFloor(1);
        List<Window> secondFloorWindows = windowService.getByFloor(2);
        model.addAttribute("firstFloorWindows", firstFloorWindows);
        model.addAttribute("secondFloorWindows", secondFloorWindows);
        return "windows";
    }
    
    @GetMapping("/window/{id}")
    public String windowDetailPage(@PathVariable Long id, Model model) {
        Window windowObj = windowService.getById(id);
        if (windowObj == null) {
            return "redirect:/windows";
        }
        model.addAttribute("windowObj", windowObj);
        return "window-detail";
    }
    
    @GetMapping("/api/windows")
    @ResponseBody
    public Result<List<Window>> getAllWindows() {
        List<Window> windows = windowService.list();
        return Result.success(windows);
    }
    
    @GetMapping("/api/windows/floor/{floor}")
    @ResponseBody
    public Result<List<Window>> getWindowsByFloor(@PathVariable Integer floor) {
        List<Window> windows = windowService.getByFloor(floor);
        return Result.success(windows);
    }
    
    @GetMapping("/api/windows/open")
    @ResponseBody
    public Result<List<Window>> getOpenWindows() {
        List<Window> windows = windowService.getOpenWindows();
        return Result.success(windows);
    }
    
    @GetMapping("/api/windows/{id}")
    @ResponseBody
    public Result<Window> getWindowById(@PathVariable Long id) {
        Window window = windowService.getById(id);
        if (window == null) {
            return Result.error("窗口不存在");
        }
        return Result.success(window);
    }
    
    @PostMapping("/api/admin/windows")
    @ResponseBody
    public Result<String> addWindow(@RequestBody Window window) {
        boolean success = windowService.save(window);
        if (success) {
            return Result.success("添加成功");
        }
        return Result.error("添加失败");
    }
    
    @PutMapping("/api/admin/windows/{id}")
    @ResponseBody
    public Result<String> updateWindow(@PathVariable Long id, @RequestBody Window window) {
        window.setId(id);
        boolean success = windowService.updateById(window);
        if (success) {
            return Result.success("更新成功");
        }
        return Result.error("更新失败");
    }
    
    @DeleteMapping("/api/admin/windows/{id}")
    @ResponseBody
    public Result<String> deleteWindow(@PathVariable Long id) {
        boolean success = windowService.removeById(id);
        if (success) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }
    
    @PostMapping("/api/admin/windows/{id}/status")
    @ResponseBody
    public Result<String> updateWindowStatus(@PathVariable Long id, @RequestParam Integer status) {
        boolean success = windowService.updateStatus(id, status);
        if (success) {
            return Result.success("状态更新成功");
        }
        return Result.error("状态更新失败");
    }
}
