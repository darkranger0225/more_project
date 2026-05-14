package com.example.electrical.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.electrical.entity.ConsumptionRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ConsumptionRecordMapper extends BaseMapper<ConsumptionRecord> {

    @Select("SELECT * FROM consumption_record WHERE student_id = #{studentId} AND deleted = 0 ORDER BY create_time DESC")
    List<ConsumptionRecord> selectByStudentId(Long studentId);
}
