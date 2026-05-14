package com.example.electrical.service;

import com.example.electrical.common.Result;
import com.example.electrical.entity.PaymentBill;

import java.util.List;

public interface PaymentService {
    
    Result<List<PaymentBill>> getBillList(Long studentId);
    
    Result<PaymentBill> getBillDetail(Long billId);
    
    Result<Void> payBill(Long studentId, Long usageId);
    
    Result<List<PaymentBill>> getAllBills();
}
