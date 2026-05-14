package com.school.communication.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.school.communication.dto.LeaveRequestDTO;
import com.school.communication.entity.LeaveRequest;
import com.school.communication.entity.Student;
import com.school.communication.entity.User;
import com.school.communication.mapper.LeaveRequestMapper;
import com.school.communication.mapper.StudentMapper;
import com.school.communication.mapper.UserMapper;
import com.school.communication.service.LeaveRequestService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaveRequestServiceImpl extends ServiceImpl<LeaveRequestMapper, LeaveRequest> implements LeaveRequestService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private StudentMapper studentMapper;
    
    @Override
    @Transactional
    public void createLeaveRequest(LeaveRequestDTO leaveRequestDTO, Long parentId) {
        LeaveRequest leaveRequest = new LeaveRequest();
        BeanUtils.copyProperties(leaveRequestDTO, leaveRequest);
        leaveRequest.setParentId(parentId);
        leaveRequest.setStatus("PENDING");
        
        long days = ChronoUnit.DAYS.between(leaveRequestDTO.getStartDate(), leaveRequestDTO.getEndDate()) + 1;
        leaveRequest.setDays((int) days);
        
        baseMapper.insert(leaveRequest);
    }
    
    @Override
    @Transactional
    public void approveLeaveRequest(LeaveRequestDTO leaveRequestDTO, Long approverId) {
        LeaveRequest leaveRequest = baseMapper.selectById(leaveRequestDTO.getId());
        if (leaveRequest == null || leaveRequest.getDeleted() != null && leaveRequest.getDeleted() == 1) {
            throw new RuntimeException("请假申请不存在或已被删除");
        }

        leaveRequest.setStatus(leaveRequestDTO.getStatus());
        leaveRequest.setApproverId(approverId);
        leaveRequest.setApproveTime(LocalDateTime.now());
        leaveRequest.setApproveRemark(leaveRequestDTO.getApproveRemark());

        baseMapper.updateById(leaveRequest);
    }
    
    @Override
    public LeaveRequestDTO getLeaveRequestById(Long id) {
        LeaveRequest leaveRequest = baseMapper.selectById(id);
        if (leaveRequest == null) {
            return null;
        }
        return convertToDTO(leaveRequest);
    }
    
    @Override
    public List<LeaveRequestDTO> getLeaveRequestList(Long userId, String role) {
        List<LeaveRequest> list;
        
        if ("PARENT".equals(role)) {
            list = baseMapper.selectByParentId(userId);
        } else if ("TEACHER".equals(role)) {
            list = baseMapper.selectByTeacherId(userId);
        } else {
            list = baseMapper.selectList(null);
        }
        
        return list.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public void deleteLeaveRequest(Long id) {
        baseMapper.deleteById(id);
    }
    
    private LeaveRequestDTO convertToDTO(LeaveRequest leaveRequest) {
        LeaveRequestDTO dto = new LeaveRequestDTO();
        BeanUtils.copyProperties(leaveRequest, dto);
        
        if (leaveRequest.getStudentId() != null) {
            Student student = studentMapper.selectById(leaveRequest.getStudentId());
            if (student != null) {
                dto.setStudentName(student.getStudentName());
            }
        }
        
        if (leaveRequest.getParentId() != null) {
            User parent = userMapper.selectById(leaveRequest.getParentId());
            if (parent != null) {
                dto.setParentName(parent.getRealName());
            }
        }
        
        if (leaveRequest.getApproverId() != null) {
            User approver = userMapper.selectById(leaveRequest.getApproverId());
            if (approver != null) {
                dto.setApproverName(approver.getRealName());
            }
        }
        
        return dto;
    }
}