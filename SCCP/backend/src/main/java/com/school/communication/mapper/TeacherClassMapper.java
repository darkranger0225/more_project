package com.school.communication.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.school.communication.entity.TeacherClass;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TeacherClassMapper extends BaseMapper<TeacherClass> {
    
    @Select("SELECT * FROM teacher_class WHERE teacher_id = #{teacherId}")
    List<TeacherClass> selectByTeacherId(Long teacherId);
    
    @Select("SELECT * FROM teacher_class WHERE class_id = #{classId}")
    List<TeacherClass> selectByClassId(Long classId);
}