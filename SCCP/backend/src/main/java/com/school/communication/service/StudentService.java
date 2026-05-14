package com.school.communication.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.school.communication.dto.StudentDTO;
import com.school.communication.entity.Student;

import java.util.List;

public interface StudentService extends IService<Student> {
    
    void createStudent(StudentDTO studentDTO);
    
    void updateStudent(StudentDTO studentDTO);
    
    StudentDTO getStudentById(Long id);
    
    List<StudentDTO> getStudentList();
    
    List<StudentDTO> getStudentListByParentId(Long parentId);
    
    List<StudentDTO> getStudentListByClassId(Long classId);
    
    List<StudentDTO> getStudentListByTeacherId(Long teacherId);
    
    void deleteStudent(Long id);
}