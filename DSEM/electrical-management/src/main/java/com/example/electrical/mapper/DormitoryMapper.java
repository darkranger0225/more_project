package com.example.electrical.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.electrical.entity.Dormitory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DormitoryMapper extends BaseMapper<Dormitory> {

    @Select("SELECT * FROM dormitory WHERE floor_id = #{floorId} AND deleted = 0 ORDER BY dormitory_number")
    List<Dormitory> selectByFloorId(Long floorId);

    @Select("SELECT * FROM dormitory WHERE building_id = #{buildingId} AND deleted = 0 ORDER BY floor_id, dormitory_number")
    List<Dormitory> selectByBuildingId(Long buildingId);
}
