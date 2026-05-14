package com.example.electrical.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.electrical.entity.Building;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BuildingMapper extends BaseMapper<Building> {

    @Select("SELECT * FROM building WHERE deleted = 0 ORDER BY building_number")
    List<Building> selectAll();
}
