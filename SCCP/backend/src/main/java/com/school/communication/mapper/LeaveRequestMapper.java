package com.school.communication.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.school.communication.entity.LeaveRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LeaveRequestMapper extends BaseMapper<LeaveRequest> {
    
    @Select("SELECT * FROM leave_request WHERE parent_id = #{parentId} AND deleted = 0 ORDER BY create_time DESC")
    List<LeaveRequest> selectByParentId(Long parentId);

    @Select("SELECT lr.* FROM leave_request lr " +
            "INNER JOIN student s ON lr.student_id = s.id " +
            "WHERE s.class_id = #{classId} AND lr.deleted = 0 ORDER BY lr.create_time DESC")
    List<LeaveRequest> selectByClassId(Long classId);

    @Select("SELECT DISTINCT lr.* FROM leave_request lr " +
            "INNER JOIN student s ON lr.student_id = s.id " +
            "LEFT JOIN teacher_class tc ON s.class_id = tc.class_id " +
            "LEFT JOIN class c ON s.class_id = c.id " +
            "WHERE (tc.teacher_id = #{teacherId} OR c.head_teacher_id = #{teacherId}) " +
            "AND lr.deleted = 0 ORDER BY lr.create_time DESC")
    List<LeaveRequest> selectByTeacherId(Long teacherId);
}