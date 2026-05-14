package com.school.communication.service;

import com.school.communication.dto.TeacherClassDTO;

import java.util.List;

public interface TeacherClassService {
    List<TeacherClassDTO> getTeachersByClassId(Long classId);
    void assignTeacher(TeacherClassDTO teacherClassDTO);
    void removeTeacher(Long id);
}
