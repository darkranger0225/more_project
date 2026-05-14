package com.school.communication.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.school.communication.dto.StudentDTO;
import com.school.communication.entity.ClassInfo;
import com.school.communication.entity.Student;
import com.school.communication.entity.User;
import com.school.communication.mapper.ClassInfoMapper;
import com.school.communication.mapper.StudentMapper;
import com.school.communication.mapper.UserMapper;
import com.school.communication.service.StudentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private ClassInfoMapper classInfoMapper;
    
    @Override
    @Transactional
    public void createStudent(StudentDTO studentDTO) {
        Student student = new Student();
        BeanUtils.copyProperties(studentDTO, student);
        
        // 如果没有提供学号，自动生成唯一学号
        if (student.getStudentNo() == null || student.getStudentNo().trim().isEmpty()) {
            student.setStudentNo(generateStudentNo());
        }
        
        baseMapper.insert(student);
    }
    
    /**
     * 生成唯一学号
     * 格式：年月日 + 4位随机数，如：202403141234
     */
    private String generateStudentNo() {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        // 生成4位随机数
        int randomNum = (int) (Math.random() * 9000) + 1000;
        String studentNo = dateStr + randomNum;
        
        // 检查是否已存在，如果存在则重新生成
        while (isStudentNoExists(studentNo)) {
            randomNum = (int) (Math.random() * 9000) + 1000;
            studentNo = dateStr + randomNum;
        }
        
        return studentNo;
    }
    
    /**
     * 检查学号是否已存在
     */
    private boolean isStudentNoExists(String studentNo) {
        return baseMapper.selectCount(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Student>()
                .eq(Student::getStudentNo, studentNo)
        ) > 0;
    }
    
    @Override
    @Transactional
    public void updateStudent(StudentDTO studentDTO) {
        Student student = baseMapper.selectById(studentDTO.getId());
        if (student == null) {
            throw new RuntimeException("学生不存在");
        }
        BeanUtils.copyProperties(studentDTO, student);
        baseMapper.updateById(student);
    }
    
    @Override
    public StudentDTO getStudentById(Long id) {
        Student student = baseMapper.selectById(id);
        if (student == null) {
            return null;
        }
        return convertToDTO(student);
    }
    
    @Override
    public List<StudentDTO> getStudentList() {
        List<Student> list = baseMapper.selectList(null);
        return list.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    
    @Override
    public List<StudentDTO> getStudentListByParentId(Long parentId) {
        List<Student> list = baseMapper.selectByParentId(parentId);
        return list.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    
    @Override
    public List<StudentDTO> getStudentListByClassId(Long classId) {
        List<Student> list = baseMapper.selectByClassId(classId);
        return list.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    
    @Override
    public List<StudentDTO> getStudentListByTeacherId(Long teacherId) {
        List<Student> list = baseMapper.selectByTeacherId(teacherId);
        return list.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public void deleteStudent(Long id) {
        baseMapper.deleteById(id);
    }
    
    private StudentDTO convertToDTO(Student student) {
        StudentDTO dto = new StudentDTO();
        BeanUtils.copyProperties(student, dto);
        
        if (student.getClassId() != null) {
            ClassInfo classInfo = classInfoMapper.selectById(student.getClassId());
            if (classInfo != null) {
                dto.setClassName(classInfo.getClassName());
            }
        }
        
        if (student.getParentId() != null) {
            User parent = userMapper.selectById(student.getParentId());
            if (parent != null) {
                dto.setParentName(parent.getRealName());
            }
        }
        
        return dto;
    }
}