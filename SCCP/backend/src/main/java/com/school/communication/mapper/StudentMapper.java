package com.school.communication.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.school.communication.entity.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StudentMapper extends BaseMapper<Student> {
    
    @Select("SELECT * FROM student WHERE parent_id = #{parentId} AND deleted = 0")
    List<Student> selectByParentId(Long parentId);

    @Select("SELECT * FROM student WHERE class_id = #{classId} AND deleted = 0")
    List<Student> selectByClassId(Long classId);

    @Select("SELECT DISTINCT s.* FROM student s " +
            "LEFT JOIN teacher_class tc ON s.class_id = tc.class_id " +
            "LEFT JOIN class c ON s.class_id = c.id " +
            "WHERE (tc.teacher_id = #{teacherId} OR c.head_teacher_id = #{teacherId}) " +
            "AND s.deleted = 0")
    List<Student> selectByTeacherId(Long teacherId);
}