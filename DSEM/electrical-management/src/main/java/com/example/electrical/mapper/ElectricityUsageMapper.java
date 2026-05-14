package com.example.electrical.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.electrical.entity.ElectricityUsage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface ElectricityUsageMapper extends BaseMapper<ElectricityUsage> {

    @Select("SELECT * FROM electricity_usage WHERE student_id = #{studentId} AND deleted = 0 ORDER BY record_time DESC")
    List<ElectricityUsage> selectByStudentId(Long studentId);

    @Select("SELECT * FROM electricity_usage WHERE floor_id = #{floorId} AND deleted = 0 ORDER BY record_time DESC")
    List<ElectricityUsage> selectByFloorId(Long floorId);

    @Select("SELECT SUM(balance) FROM electricity_usage WHERE student_id = #{studentId} AND status = 0 AND deleted = 0")
    BigDecimal selectUnpaidAmountByStudentId(Long studentId);

    @Select("SELECT * FROM electricity_usage WHERE student_id = #{studentId} AND status = 0 AND deleted = 0 ORDER BY record_time DESC")
    List<ElectricityUsage> selectUnpaidByStudentId(Long studentId);

    // 根据宿舍ID查询用电记录（用于同寝室用户查看）
    @Select("SELECT * FROM electricity_usage WHERE dormitory_id = #{dormitoryId} AND deleted = 0 ORDER BY record_time DESC")
    List<ElectricityUsage> selectByDormitoryId(@Param("dormitoryId") Long dormitoryId);

    // 根据宿舍ID查询待缴费记录
    @Select("SELECT * FROM electricity_usage WHERE dormitory_id = #{dormitoryId} AND status = 0 AND deleted = 0 ORDER BY record_time DESC")
    List<ElectricityUsage> selectUnpaidByDormitoryId(@Param("dormitoryId") Long dormitoryId);
}
