package com.example.electrical.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RepairHandleDTO {
    
    @NotNull(message = "报修ID不能为空")
    private Long repairId;
    
    @NotNull(message = "处理状态不能为空")
    private Integer status;
    
    private String reply;
}
