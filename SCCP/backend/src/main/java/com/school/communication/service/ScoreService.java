package com.school.communication.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.school.communication.dto.ScoreDTO;
import com.school.communication.entity.Score;

import java.util.List;

public interface ScoreService extends IService<Score> {
    
    void createScore(ScoreDTO scoreDTO, Long teacherId);
    
    void updateScore(ScoreDTO scoreDTO);
    
    ScoreDTO getScoreById(Long id);
    
    List<ScoreDTO> getScoreList(Long userId, String role);
    
    List<ScoreDTO> getScoreListByStudentId(Long studentId);
    
    List<ScoreDTO> getScoreListByClassIdAndExamName(Long classId, String examName);
    
    List<String> getExamNameList();
    
    void deleteScore(Long id);
}