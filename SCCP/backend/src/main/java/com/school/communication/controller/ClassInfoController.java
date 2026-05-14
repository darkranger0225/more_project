package com.school.communication.controller;

import com.school.communication.dto.ClassInfoDTO;
import com.school.communication.dto.Result;
import com.school.communication.service.ClassInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/class")
public class ClassInfoController {
    
    @Autowired
    private ClassInfoService classInfoService;
    
    @GetMapping("/list")
    public Result<List<ClassInfoDTO>> getClassList() {
        List<ClassInfoDTO> list = classInfoService.getClassList();
        return Result.success(list);
    }
    
    @GetMapping("/my-classes")
    public Result<List<ClassInfoDTO>> getMyClasses(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        String role = (String) request.getAttribute("role");
        
        List<ClassInfoDTO> list;
        if ("TEACHER".equals(role)) {
            list = classInfoService.getClassListByTeacherId(userId);
        } else {
            list = classInfoService.getClassList();
        }
        return Result.success(list);
    }
    
    @GetMapping("/{id}")
    public Result<ClassInfoDTO> getClassById(@PathVariable Long id) {
        ClassInfoDTO classInfo = classInfoService.getClassById(id);
        return Result.success(classInfo);
    }
    
    @PostMapping
    public Result<Void> createClass(@Validated @RequestBody ClassInfoDTO classInfoDTO) {
        classInfoService.createClass(classInfoDTO);
        return Result.success();
    }
    
    @PutMapping
    public Result<Void> updateClass(@Validated @RequestBody ClassInfoDTO classInfoDTO) {
        classInfoService.updateClass(classInfoDTO);
        return Result.success();
    }
    
    @DeleteMapping("/{id}")
    public Result<Void> deleteClass(@PathVariable Long id) {
        classInfoService.deleteClass(id);
        return Result.success();
    }
}