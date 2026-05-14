package com.school.communication.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.school.communication.dto.HomeworkDTO;
import com.school.communication.dto.HomeworkSubmitDTO;
import com.school.communication.entity.Homework;

import java.util.List;

public interface HomeworkService extends IService<Homework> {
    
    void createHomework(HomeworkDTO homeworkDTO, Long teacherId);
    
    void updateHomework(HomeworkDTO homeworkDTO);
    
    HomeworkDTO getHomeworkById(Long id);
    
    List<HomeworkDTO> getHomeworkList(Long userId, String role);
    
    void deleteHomework(Long id);
    
    void submitHomework(HomeworkSubmitDTO submitDTO);
    
    void gradeHomework(HomeworkSubmitDTO submitDTO);
    
    List<HomeworkSubmitDTO> getSubmitList(Long homeworkId);

    List<HomeworkSubmitDTO> getSubmitListWithStudents(Long homeworkId);

    HomeworkSubmitDTO getSubmitByStudent(Long homeworkId, Long studentId);
}