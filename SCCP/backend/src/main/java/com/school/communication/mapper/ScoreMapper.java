package com.school.communication.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.school.communication.entity.Score;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ScoreMapper extends BaseMapper<Score> {
    
    @Select("SELECT * FROM score WHERE student_id = #{studentId} AND deleted = 0 ORDER BY exam_date DESC")
    List<Score> selectByStudentId(Long studentId);

    @Select("SELECT s.* FROM score s " +
            "INNER JOIN student st ON s.student_id = st.id " +
            "WHERE st.class_id = #{classId} AND s.exam_name = #{examName} AND s.deleted = 0")
    List<Score> selectByClassIdAndExamName(@Param("classId") Long classId, @Param("examName") String examName);

    @Select("SELECT DISTINCT exam_name FROM score WHERE deleted = 0 ORDER BY exam_date DESC")
    List<String> selectAllExamNames();
}