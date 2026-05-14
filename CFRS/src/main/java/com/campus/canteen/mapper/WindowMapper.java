package com.campus.canteen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campus.canteen.entity.Window;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface WindowMapper extends BaseMapper<Window> {

    @Select("SELECT * FROM `window` WHERE floor = #{floor} AND deleted = 0 ORDER BY window_number")
    List<Window> selectByFloor(Integer floor);

    @Select("SELECT * FROM `window` WHERE status = 1 AND deleted = 0 ORDER BY floor, window_number")
    List<Window> selectOpenWindows();
}
