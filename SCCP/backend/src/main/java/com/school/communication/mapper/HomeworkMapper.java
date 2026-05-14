package com.school.communication.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.school.communication.entity.Homework;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface HomeworkMapper extends BaseMapper<Homework> {

    @Select("SELECT * FROM homework WHERE class_id = #{classId} AND deleted = 0 ORDER BY create_time DESC")
    List<Homework> selectByClassId(Long classId);

    @Select("<script>SELECT * FROM homework WHERE class_id IN " +
            "<foreach collection='list' item='id' open='(' separator=',' close=')'>#{id}</foreach> " +
            "AND deleted = 0 ORDER BY create_time DESC</script>")
    List<Homework> selectByClassIds(@Param("list") List<Long> classIds);

    @Select("SELECT DISTINCT h.* FROM homework h " +
            "LEFT JOIN teacher_class tc ON h.class_id = tc.class_id " +
            "WHERE (h.teacher_id = #{teacherId} OR tc.teacher_id = #{teacherId}) " +
            "AND h.deleted = 0 ORDER BY h.create_time DESC")
    List<Homework> selectByTeacherId(Long teacherId);
}