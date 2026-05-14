package com.example.electrical.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.electrical.entity.RepairRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RepairRecordMapper extends BaseMapper<RepairRecord> {
    
    @Select("SELECT * FROM repair_record WHERE student_id = #{studentId} AND deleted = 0 ORDER BY create_time DESC")
    List<RepairRecord> selectByStudentId(Long studentId);
    
    @Select("SELECT * FROM repair_record WHERE status = #{status} AND deleted = 0 ORDER BY create_time DESC")
    List<RepairRecord> selectByStatus(Integer status);
}
