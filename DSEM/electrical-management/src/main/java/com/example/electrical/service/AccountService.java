package com.example.electrical.service;

import com.example.electrical.common.Result;
import com.example.electrical.dto.LoginDeductionDTO;
import com.example.electrical.entity.ConsumptionRecord;
import com.example.electrical.entity.RechargeRecord;
import com.example.electrical.entity.UserAccount;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {

    Result<UserAccount> getAccountInfo(Long studentId);

    Result<String> createRechargeOrder(Long studentId, BigDecimal amount);

    Result<Void> confirmRecharge(String rechargeNo);

    Result<List<RechargeRecord>> getRechargeRecords(Long studentId);

    Result<List<ConsumptionRecord>> getConsumptionRecords(Long studentId);

    Result<LoginDeductionDTO> deductForLogin(Long studentId);
}
