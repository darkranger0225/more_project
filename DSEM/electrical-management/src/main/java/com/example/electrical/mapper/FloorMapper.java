package com.example.electrical.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.electrical.entity.Floor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FloorMapper extends BaseMapper<Floor> {

    @Select("SELECT DISTINCT building_id as buildingId FROM floor_info WHERE deleted = 0 ORDER BY building_id")
    List<Long> selectAllBuildingIds();

    @Select("SELECT * FROM floor_info WHERE building_id = #{buildingId} AND deleted = 0 ORDER BY floor_number")
    List<Floor> selectByBuildingId(Long buildingId);
}
