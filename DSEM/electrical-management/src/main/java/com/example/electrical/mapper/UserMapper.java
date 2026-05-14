package com.example.electrical.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.electrical.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT * FROM sys_user WHERE account = #{account} AND deleted = 0")
    User selectByAccount(String account);

    @Select("SELECT * FROM sys_user WHERE dormitory_id = #{dormitoryId} AND deleted = 0 AND status = 0")
    List<User> selectByDormitoryId(@Param("dormitoryId") Long dormitoryId);
}
