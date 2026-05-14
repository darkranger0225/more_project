package com.school.communication.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.school.communication.dto.ClassInfoDTO;
import com.school.communication.entity.ClassInfo;

import java.util.List;

public interface ClassInfoService extends IService<ClassInfo> {
    
    void createClass(ClassInfoDTO classInfoDTO);
    
    void updateClass(ClassInfoDTO classInfoDTO);
    
    ClassInfoDTO getClassById(Long id);
    
    List<ClassInfoDTO> getClassList();
    
    List<ClassInfoDTO> getClassListByTeacherId(Long teacherId);
    
    void deleteClass(Long id);
}