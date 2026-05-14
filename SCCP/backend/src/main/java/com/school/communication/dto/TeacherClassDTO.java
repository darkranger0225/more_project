package com.school.communication.dto;

import lombok.Data;

@Data
public class TeacherClassDTO {
    private Long id;
    private Long teacherId;
    private Long classId;
    private String teacherName;
    private String subject;
}
