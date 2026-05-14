package com.school.communication.controller;

import com.school.communication.dto.Result;
import com.school.communication.dto.TeacherClassDTO;
import com.school.communication.service.TeacherClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teacher-class")
public class TeacherClassController {

    @Autowired
    private TeacherClassService teacherClassService;

    @GetMapping("/class/{classId}")
    public Result<List<TeacherClassDTO>> getTeachersByClassId(@PathVariable Long classId) {
        List<TeacherClassDTO> list = teacherClassService.getTeachersByClassId(classId);
        return Result.success(list);
    }

    @PostMapping
    public Result<Void> assignTeacher(@RequestBody TeacherClassDTO teacherClassDTO) {
        teacherClassService.assignTeacher(teacherClassDTO);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> removeTeacher(@PathVariable Long id) {
        teacherClassService.removeTeacher(id);
        return Result.success();
    }
}
