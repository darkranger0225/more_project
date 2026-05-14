package com.school.communication.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.school.communication.entity.HomeworkSubmit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface HomeworkSubmitMapper extends BaseMapper<HomeworkSubmit> {
    
    @Select("SELECT * FROM homework_submit WHERE homework_id = #{homeworkId}")
    List<HomeworkSubmit> selectByHomeworkId(Long homeworkId);
    
    @Select("SELECT * FROM homework_submit WHERE student_id = #{studentId} ORDER BY submit_time DESC")
    List<HomeworkSubmit> selectByStudentId(Long studentId);
    
    @Select("SELECT hs.* FROM homework_submit hs " +
            "INNER JOIN student s ON hs.student_id = s.id " +
            "WHERE hs.homework_id = #{homeworkId} AND s.class_id = #{classId}")
    List<HomeworkSubmit> selectByHomeworkIdAndClassId(Long homeworkId, Long classId);
}