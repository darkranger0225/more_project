package com.example.electrical.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.electrical.entity.SystemConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SystemConfigMapper extends BaseMapper<SystemConfig> {
    
    @Select("SELECT * FROM system_config WHERE config_key = #{key} AND deleted = 0")
    SystemConfig selectByKey(String key);
}
