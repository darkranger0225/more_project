package com.example.electrical.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.electrical.entity.RechargeRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RechargeRecordMapper extends BaseMapper<RechargeRecord> {

    @Select("SELECT * FROM recharge_record WHERE student_id = #{studentId} AND deleted = 0 ORDER BY create_time DESC")
    List<RechargeRecord> selectByStudentId(Long studentId);
}
