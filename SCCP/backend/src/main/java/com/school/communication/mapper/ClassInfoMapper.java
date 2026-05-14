package com.school.communication.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.school.communication.entity.ClassInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ClassInfoMapper extends BaseMapper<ClassInfo> {
    
    @Select("SELECT c.* FROM class c " +
            "LEFT JOIN teacher_class tc ON c.id = tc.class_id " +
            "WHERE (tc.teacher_id = #{teacherId} OR c.head_teacher_id = #{teacherId}) " +
            "AND c.deleted = 0")
    List<ClassInfo> selectByTeacherId(Long teacherId);
}