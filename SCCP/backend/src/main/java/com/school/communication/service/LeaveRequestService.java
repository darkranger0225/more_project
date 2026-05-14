package com.school.communication.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.school.communication.dto.LeaveRequestDTO;
import com.school.communication.entity.LeaveRequest;

import java.util.List;

public interface LeaveRequestService extends IService<LeaveRequest> {
    
    void createLeaveRequest(LeaveRequestDTO leaveRequestDTO, Long parentId);
    
    void approveLeaveRequest(LeaveRequestDTO leaveRequestDTO, Long approverId);
    
    LeaveRequestDTO getLeaveRequestById(Long id);
    
    List<LeaveRequestDTO> getLeaveRequestList(Long userId, String role);
    
    void deleteLeaveRequest(Long id);
}