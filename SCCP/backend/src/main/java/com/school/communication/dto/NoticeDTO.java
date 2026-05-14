package com.school.communication.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class NoticeDTO {
    private Long id;
    
    @NotBlank(message = "标题不能为空")
    private String title;
    
    @NotBlank(message = "内容不能为空")
    private String content;
    
    private Long publisherId;
    
    private String publisherName;
    
    @NotBlank(message = "目标类型不能为空")
    private String targetType;
    
    private String targetIds;
    
    private Integer priority;
    
    private Integer readCount;
    
    private Integer status;
    
    private LocalDateTime publishTime;
    
    private LocalDateTime createTime;
    
    private Boolean isRead;
}