package com.example.electrical.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.electrical.common.Constants;
import com.example.electrical.common.Result;
import com.example.electrical.dto.RepairDTO;
import com.example.electrical.dto.RepairHandleDTO;
import com.example.electrical.entity.RepairRecord;
import com.example.electrical.mapper.RepairRecordMapper;
import com.example.electrical.service.RepairService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RepairServiceImpl extends ServiceImpl<RepairRecordMapper, RepairRecord> implements RepairService {

    @Autowired
    private RepairRecordMapper repairRecordMapper;

    @Override
    public Result<Void> submitRepair(Long studentId, RepairDTO repairDTO) {
        RepairRecord record = new RepairRecord();
        BeanUtils.copyProperties(repairDTO, record);
        record.setStudentId(studentId);
        record.setStatus(Constants.REPAIR_STATUS_PENDING);

        repairRecordMapper.insert(record);
        return Result.success();
    }

    @Override
    public Result<List<RepairRecord>> getMyRepairs(Long studentId) {
        List<RepairRecord> list = repairRecordMapper.selectByStudentId(studentId);
        return Result.success(list);
    }

    @Override
    public Result<RepairRecord> getRepairDetail(Long repairId) {
        RepairRecord record = repairRecordMapper.selectById(repairId);
        if (record == null) {
            return Result.error("报修记录不存在");
        }
        return Result.success(record);
    }

    @Override
    public Result<List<RepairRecord>> getPendingRepairs() {
        List<RepairRecord> list = repairRecordMapper.selectByStatus(Constants.REPAIR_STATUS_PENDING);
        return Result.success(list);
    }

    @Override
    public Result<List<RepairRecord>> getAllRepairs() {
        List<RepairRecord> list = repairRecordMapper.selectList(null);
        return Result.success(list);
    }

    @Override
    public Result<Void> handleRepair(Long adminId, RepairHandleDTO handleDTO) {
        RepairRecord record = repairRecordMapper.selectById(handleDTO.getRepairId());
        if (record == null) {
            return Result.error("报修记录不存在");
        }

        record.setStatus(handleDTO.getStatus());
        record.setReply(handleDTO.getReply());
        record.setHandlerId(adminId);
        record.setHandleTime(LocalDateTime.now());

        repairRecordMapper.updateById(record);
        return Result.success();
    }
}
