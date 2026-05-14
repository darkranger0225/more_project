package com.example.electrical.service;

import com.example.electrical.common.Result;
import com.example.electrical.dto.ElectricityUsageDTO;
import com.example.electrical.entity.ElectricityUsage;

import java.math.BigDecimal;
import java.util.List;

public interface ElectricityService {
    
    Result<List<ElectricityUsage>> getUsageList(Long studentId);
    
    Result<BigDecimal> getBalance(Long studentId);
    
    Result<List<ElectricityUsage>> getUnpaidList(Long studentId);
    
    Result<Void> addUsage(Long adminId, ElectricityUsageDTO usageDTO);
    
    Result<List<ElectricityUsage>> getUsageByFloor(Long floorId);
}
