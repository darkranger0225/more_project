package com.school.communication.controller;

import com.school.communication.dto.Result;
import com.school.communication.dto.ScoreDTO;
import com.school.communication.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/score")
public class ScoreController {
    
    @Autowired
    private ScoreService scoreService;
    
    @GetMapping("/list")
    public Result<List<ScoreDTO>> getScoreList(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        String role = (String) request.getAttribute("role");
        List<ScoreDTO> list = scoreService.getScoreList(userId, role);
        return Result.success(list);
    }
    
    @GetMapping("/student/{studentId}")
    public Result<List<ScoreDTO>> getScoreListByStudentId(@PathVariable Long studentId) {
        List<ScoreDTO> list = scoreService.getScoreListByStudentId(studentId);
        return Result.success(list);
    }
    
    @GetMapping("/class/{classId}/exam/{examName}")
    public Result<List<ScoreDTO>> getScoreListByClassIdAndExamName(
            @PathVariable Long classId, @PathVariable String examName) {
        List<ScoreDTO> list = scoreService.getScoreListByClassIdAndExamName(classId, examName);
        return Result.success(list);
    }
    
    @GetMapping("/exam-names")
    public Result<List<String>> getExamNameList() {
        List<String> list = scoreService.getExamNameList();
        return Result.success(list);
    }
    
    @GetMapping("/{id}")
    public Result<ScoreDTO> getScoreById(@PathVariable Long id) {
        ScoreDTO score = scoreService.getScoreById(id);
        return Result.success(score);
    }
    
    @PostMapping
    public Result<Void> createScore(@Validated @RequestBody ScoreDTO scoreDTO, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        scoreService.createScore(scoreDTO, userId);
        return Result.success();
    }
    
    @PutMapping
    public Result<Void> updateScore(@Validated @RequestBody ScoreDTO scoreDTO) {
        scoreService.updateScore(scoreDTO);
        return Result.success();
    }
    
    @DeleteMapping("/{id}")
    public Result<Void> deleteScore(@PathVariable Long id) {
        scoreService.deleteScore(id);
        return Result.success();
    }
}