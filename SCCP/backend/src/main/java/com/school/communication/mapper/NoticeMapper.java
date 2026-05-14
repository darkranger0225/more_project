package com.school.communication.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.school.communication.entity.Notice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface NoticeMapper extends BaseMapper<Notice> {
    
    @Select("SELECT n.* FROM notice n " +
            "WHERE n.status = 1 AND n.deleted = 0 " +
            "AND (n.target_type = 'ALL' " +
            "OR (n.target_type = 'CLASS' AND FIND_IN_SET(#{classId}, n.target_ids)) " +
            "OR (n.target_type = 'PARENT' AND FIND_IN_SET(#{parentId}, n.target_ids))) " +
            "ORDER BY n.priority DESC, n.create_time DESC")
    List<Notice> selectByTargetUser(@Param("classId") Long classId, @Param("parentId") Long parentId);
    
    @Select("SELECT DISTINCT n.* FROM notice n " +
            "LEFT JOIN teacher_class tc ON n.target_type = 'CLASS' AND FIND_IN_SET(tc.class_id, n.target_ids) " +
            "WHERE n.status = 1 AND n.deleted = 0 " +
            "AND (n.target_type = 'ALL' " +
            "OR (n.target_type = 'CLASS' AND tc.teacher_id = #{teacherId}) " +
            "OR n.publisher_id = #{teacherId}) " +
            "ORDER BY n.priority DESC, n.create_time DESC")
    List<Notice> selectByTargetTeacher(@Param("teacherId") Long teacherId);
    
    @Update("UPDATE notice SET read_count = read_count + 1 WHERE id = #{noticeId}")
    void incrementReadCount(Long noticeId);
    
    @Select("SELECT * FROM notice WHERE publisher_id = #{publisherId} AND deleted = 0 ORDER BY create_time DESC")
    List<Notice> selectByPublisher(@Param("publisherId") Long publisherId);
}