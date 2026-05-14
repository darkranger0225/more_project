package com.example.electrical.service;

import com.example.electrical.common.Result;
import com.example.electrical.dto.RepairDTO;
import com.example.electrical.dto.RepairHandleDTO;
import com.example.electrical.entity.RepairRecord;

import java.util.List;

public interface RepairService {
    
    Result<Void> submitRepair(Long studentId, RepairDTO repairDTO);
    
    Result<List<RepairRecord>> getMyRepairs(Long studentId);
    
    Result<RepairRecord> getRepairDetail(Long repairId);
    
    Result<List<RepairRecord>> getPendingRepairs();
    
    Result<List<RepairRecord>> getAllRepairs();
    
    Result<Void> handleRepair(Long adminId, RepairHandleDTO handleDTO);
}
