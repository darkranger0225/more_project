package com.example.electrical.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RepairDTO {
    
    @NotBlank(message = "标题不能为空")
    private String title;
    
    @NotBlank(message = "内容不能为空")
    private String content;
    
    private String image;
}
