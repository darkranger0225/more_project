package com.example.electrical.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.electrical.entity.UserAccount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;

@Mapper
public interface UserAccountMapper extends BaseMapper<UserAccount> {

    @Select("SELECT * FROM user_account WHERE student_id = #{studentId} AND deleted = 0")
    UserAccount selectByStudentId(Long studentId);

    @Update("UPDATE user_account SET balance = balance + #{amount}, total_recharge = total_recharge + #{amount}, update_time = NOW() WHERE student_id = #{studentId}")
    int addBalance(@Param("studentId") Long studentId, @Param("amount") BigDecimal amount);

    @Update("UPDATE user_account SET balance = balance - #{amount}, total_consumption = total_consumption + #{amount}, update_time = NOW() WHERE student_id = #{studentId} AND balance >= #{amount}")
    int deductBalance(@Param("studentId") Long studentId, @Param("amount") BigDecimal amount);
}
