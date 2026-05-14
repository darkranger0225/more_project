package com.school.communication.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("leave_request")
public class LeaveRequest {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long studentId;
    
    private Long parentId;
    
    private String leaveType;
    
    private LocalDate startDate;
    
    private LocalDate endDate;
    
    private Integer days;
    
    private String reason;
    
    private String attachmentUrl;
    
    private String status;
    
    private Long approverId;
    
    private LocalDateTime approveTime;
    
    private String approveRemark;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    @TableLogic
    @TableField(select = false)
    private Integer deleted;
}