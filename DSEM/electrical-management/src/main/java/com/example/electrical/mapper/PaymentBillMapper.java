package com.example.electrical.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.electrical.entity.PaymentBill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PaymentBillMapper extends BaseMapper<PaymentBill> {
    
    @Select("SELECT * FROM payment_bill WHERE student_id = #{studentId} AND deleted = 0 ORDER BY create_time DESC")
    List<PaymentBill> selectByStudentId(Long studentId);
    
    @Select("SELECT * FROM payment_bill WHERE remark = #{remark} AND deleted = 0 ORDER BY create_time DESC")
    List<PaymentBill> selectByRemark(String remark);
    
    @Select("SELECT * FROM payment_bill WHERE remark LIKE CONCAT('%', #{remark}, '%') AND deleted = 0 ORDER BY create_time DESC")
    List<PaymentBill> selectByRemarkLike(String remark);
}
