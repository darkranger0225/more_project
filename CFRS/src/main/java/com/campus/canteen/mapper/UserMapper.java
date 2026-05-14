package com.campus.canteen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campus.canteen.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    
    @Select("SELECT * FROM user WHERE student_id = #{studentId} AND deleted = 0")
    User selectByStudentId(String studentId);
    
    @Select("SELECT * FROM user WHERE username = #{username} AND deleted = 0")
    User selectByUsername(String username);
    
    @Select("SELECT * FROM user WHERE phone = #{phone} AND deleted = 0")
    User selectByPhone(String phone);
    
    @Select("SELECT * FROM user WHERE email = #{email} AND deleted = 0")
    User selectByEmail(String email);
}
