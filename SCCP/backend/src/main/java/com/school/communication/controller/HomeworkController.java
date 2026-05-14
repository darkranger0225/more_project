package com.school.communication.controller;

import com.school.communication.dto.HomeworkDTO;
import com.school.communication.dto.HomeworkSubmitDTO;
import com.school.communication.dto.Result;
import com.school.communication.service.HomeworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/homework")
public class HomeworkController {
    
    @Autowired
    private HomeworkService homeworkService;
    
    @GetMapping("/list")
    public Result<List<HomeworkDTO>> getHomeworkList(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        String role = (String) request.getAttribute("role");
        List<HomeworkDTO> list = homeworkService.getHomeworkList(userId, role);
        return Result.success(list);
    }
    
    @GetMapping("/{id}")
    public Result<HomeworkDTO> getHomeworkById(@PathVariable Long id) {
        HomeworkDTO homework = homeworkService.getHomeworkById(id);
        return Result.success(homework);
    }
    
    @PostMapping
    public Result<Void> createHomework(@Validated @RequestBody HomeworkDTO homeworkDTO, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        homeworkService.createHomework(homeworkDTO, userId);
        return Result.success();
    }
    
    @PutMapping
    public Result<Void> updateHomework(@Validated @RequestBody HomeworkDTO homeworkDTO) {
        homeworkService.updateHomework(homeworkDTO);
        return Result.success();
    }
    
    @DeleteMapping("/{id}")
    public Result<Void> deleteHomework(@PathVariable Long id) {
        homeworkService.deleteHomework(id);
        return Result.success();
    }
    
    @PostMapping("/submit")
    public Result<Void> submitHomework(@RequestBody HomeworkSubmitDTO submitDTO) {
        homeworkService.submitHomework(submitDTO);
        return Result.success();
    }
    
    @PostMapping("/grade")
    public Result<Void> gradeHomework(@RequestBody HomeworkSubmitDTO submitDTO) {
        homeworkService.gradeHomework(submitDTO);
        return Result.success();
    }
    
    @GetMapping("/{id}/submits")
    public Result<List<HomeworkSubmitDTO>> getSubmitList(@PathVariable Long id) {
        List<HomeworkSubmitDTO> list = homeworkService.getSubmitListWithStudents(id);
        return Result.success(list);
    }
    
    @GetMapping("/{homeworkId}/submit/{studentId}")
    public Result<HomeworkSubmitDTO> getSubmitByStudent(@PathVariable Long homeworkId, @PathVariable Long studentId) {
        HomeworkSubmitDTO submit = homeworkService.getSubmitByStudent(homeworkId, studentId);
        return Result.success(submit);
    }
}