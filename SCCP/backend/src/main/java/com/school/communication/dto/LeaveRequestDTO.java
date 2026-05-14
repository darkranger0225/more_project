package com.school.communication.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class LeaveRequestDTO {
    private Long id;
    
    @NotNull(message = "学生不能为空")
    private Long studentId;
    
    private String studentName;
    
    private Long parentId;
    
    private String parentName;
    
    @NotBlank(message = "请假类型不能为空")
    private String leaveType;
    
    @NotNull(message = "开始日期不能为空")
    private LocalDate startDate;
    
    @NotNull(message = "结束日期不能为空")
    private LocalDate endDate;
    
    private Integer days;
    
    @NotBlank(message = "请假原因不能为空")
    private String reason;
    
    private String attachmentUrl;
    
    private String status;
    
    private Long approverId;
    
    private String approverName;
    
    private LocalDateTime approveTime;
    
    private String approveRemark;
    
    private LocalDateTime createTime;
}