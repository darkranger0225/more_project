package com.school.communication.controller;

import com.school.communication.dto.LeaveRequestDTO;
import com.school.communication.dto.Result;
import com.school.communication.service.LeaveRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/leave")
public class LeaveRequestController {
    
    @Autowired
    private LeaveRequestService leaveRequestService;
    
    @GetMapping("/list")
    public Result<List<LeaveRequestDTO>> getLeaveRequestList(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        String role = (String) request.getAttribute("role");
        List<LeaveRequestDTO> list = leaveRequestService.getLeaveRequestList(userId, role);
        return Result.success(list);
    }
    
    @GetMapping("/{id}")
    public Result<LeaveRequestDTO> getLeaveRequestById(@PathVariable Long id) {
        LeaveRequestDTO leaveRequest = leaveRequestService.getLeaveRequestById(id);
        return Result.success(leaveRequest);
    }
    
    @PostMapping
    public Result<Void> createLeaveRequest(@Validated @RequestBody LeaveRequestDTO leaveRequestDTO, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        leaveRequestService.createLeaveRequest(leaveRequestDTO, userId);
        return Result.success();
    }
    
    @PostMapping("/approve")
    public Result<Void> approveLeaveRequest(@RequestBody LeaveRequestDTO leaveRequestDTO, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        leaveRequestService.approveLeaveRequest(leaveRequestDTO, userId);
        return Result.success();
    }
    
    @DeleteMapping("/{id}")
    public Result<Void> deleteLeaveRequest(@PathVariable Long id) {
        leaveRequestService.deleteLeaveRequest(id);
        return Result.success();
    }
}