package com.school.communication.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.school.communication.dto.ClassInfoDTO;
import com.school.communication.entity.ClassInfo;
import com.school.communication.entity.User;
import com.school.communication.mapper.ClassInfoMapper;
import com.school.communication.mapper.UserMapper;
import com.school.communication.service.ClassInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClassInfoServiceImpl extends ServiceImpl<ClassInfoMapper, ClassInfo> implements ClassInfoService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Override
    @Transactional
    public void createClass(ClassInfoDTO classInfoDTO) {
        ClassInfo classInfo = new ClassInfo();
        BeanUtils.copyProperties(classInfoDTO, classInfo);
        baseMapper.insert(classInfo);
    }
    
    @Override
    @Transactional
    public void updateClass(ClassInfoDTO classInfoDTO) {
        ClassInfo classInfo = baseMapper.selectById(classInfoDTO.getId());
        if (classInfo == null) {
            throw new RuntimeException("班级不存在");
        }
        BeanUtils.copyProperties(classInfoDTO, classInfo);
        baseMapper.updateById(classInfo);
    }
    
    @Override
    public ClassInfoDTO getClassById(Long id) {
        ClassInfo classInfo = baseMapper.selectById(id);
        if (classInfo == null) {
            return null;
        }
        return convertToDTO(classInfo);
    }
    
    @Override
    public List<ClassInfoDTO> getClassList() {
        List<ClassInfo> list = baseMapper.selectList(null);
        return list.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    
    @Override
    public List<ClassInfoDTO> getClassListByTeacherId(Long teacherId) {
        List<ClassInfo> list = baseMapper.selectByTeacherId(teacherId);
        return list.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public void deleteClass(Long id) {
        baseMapper.deleteById(id);
    }
    
    private ClassInfoDTO convertToDTO(ClassInfo classInfo) {
        ClassInfoDTO dto = new ClassInfoDTO();
        BeanUtils.copyProperties(classInfo, dto);
        if (classInfo.getHeadTeacherId() != null) {
            User teacher = userMapper.selectById(classInfo.getHeadTeacherId());
            if (teacher != null) {
                dto.setHeadTeacherName(teacher.getRealName());
            }
        }
        return dto;
    }
}