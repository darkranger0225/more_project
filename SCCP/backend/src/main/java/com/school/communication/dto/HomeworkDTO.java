package com.school.communication.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class HomeworkDTO {
    private Long id;
    
    @NotBlank(message = "标题不能为空")
    private String title;
    
    @NotBlank(message = "内容不能为空")
    private String content;
    
    @NotBlank(message = "科目不能为空")
    private String subject;
    
    private Long teacherId;
    
    private String teacherName;
    
    @NotNull(message = "班级不能为空")
    private Long classId;
    
    private String className;
    
    private String attachmentUrl;
    
    @NotNull(message = "截止时间不能为空")
    private LocalDateTime deadline;
    
    private Integer status;
    
    private LocalDateTime createTime;
    
    private Integer submitCount;
    
    private Integer totalCount;
    
    private Integer submitStatus;

    private String submitStudentName;
}