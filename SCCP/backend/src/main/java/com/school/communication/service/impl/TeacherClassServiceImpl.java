package com.school.communication.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.school.communication.dto.TeacherClassDTO;
import com.school.communication.entity.TeacherClass;
import com.school.communication.entity.User;
import com.school.communication.mapper.TeacherClassMapper;
import com.school.communication.mapper.UserMapper;
import com.school.communication.service.TeacherClassService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeacherClassServiceImpl extends ServiceImpl<TeacherClassMapper, TeacherClass> implements TeacherClassService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<TeacherClassDTO> getTeachersByClassId(Long classId) {
        List<TeacherClass> list = baseMapper.selectByClassId(classId);
        return list.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void assignTeacher(TeacherClassDTO teacherClassDTO) {
        // 检查是否已存在
        TeacherClass existing = baseMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<TeacherClass>()
                        .eq(TeacherClass::getTeacherId, teacherClassDTO.getTeacherId())
                        .eq(TeacherClass::getClassId, teacherClassDTO.getClassId())
        );
        if (existing != null) {
            throw new RuntimeException("该教师已分配到此班级");
        }

        TeacherClass teacherClass = new TeacherClass();
        BeanUtils.copyProperties(teacherClassDTO, teacherClass);
        baseMapper.insert(teacherClass);
    }

    @Override
    @Transactional
    public void removeTeacher(Long id) {
        baseMapper.deleteById(id);
    }

    private TeacherClassDTO convertToDTO(TeacherClass teacherClass) {
        TeacherClassDTO dto = new TeacherClassDTO();
        BeanUtils.copyProperties(teacherClass, dto);
        // 查询教师姓名
        User teacher = userMapper.selectById(teacherClass.getTeacherId());
        if (teacher != null) {
            dto.setTeacherName(teacher.getRealName());
        }
        return dto;
    }
}
