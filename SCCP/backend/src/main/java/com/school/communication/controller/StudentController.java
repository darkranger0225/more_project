package com.school.communication.controller;

import com.school.communication.dto.Result;
import com.school.communication.dto.StudentDTO;
import com.school.communication.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {
    
    @Autowired
    private StudentService studentService;
    
    @GetMapping("/list")
    public Result<List<StudentDTO>> getStudentList() {
        List<StudentDTO> list = studentService.getStudentList();
        return Result.success(list);
    }
    
    @GetMapping("/my-students")
    public Result<List<StudentDTO>> getMyStudents(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        String role = (String) request.getAttribute("role");
        
        List<StudentDTO> list;
        if ("PARENT".equals(role)) {
            list = studentService.getStudentListByParentId(userId);
        } else if ("TEACHER".equals(role)) {
            list = studentService.getStudentListByTeacherId(userId);
        } else {
            list = studentService.getStudentList();
        }
        return Result.success(list);
    }
    
    @GetMapping("/class/{classId}")
    public Result<List<StudentDTO>> getStudentsByClassId(@PathVariable Long classId) {
        List<StudentDTO> list = studentService.getStudentListByClassId(classId);
        return Result.success(list);
    }
    
    @GetMapping("/{id}")
    public Result<StudentDTO> getStudentById(@PathVariable Long id) {
        StudentDTO student = studentService.getStudentById(id);
        return Result.success(student);
    }
    
    @PostMapping
    public Result<Void> createStudent(@Validated @RequestBody StudentDTO studentDTO) {
        studentService.createStudent(studentDTO);
        return Result.success();
    }
    
    @PutMapping
    public Result<Void> updateStudent(@Validated @RequestBody StudentDTO studentDTO) {
        studentService.updateStudent(studentDTO);
        return Result.success();
    }
    
    @DeleteMapping("/{id}")
    public Result<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return Result.success();
    }
}