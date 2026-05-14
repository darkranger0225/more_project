package com.school.communication.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.school.communication.dto.HomeworkDTO;
import com.school.communication.dto.HomeworkSubmitDTO;
import com.school.communication.entity.*;
import com.school.communication.mapper.*;
import com.school.communication.service.HomeworkService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HomeworkServiceImpl extends ServiceImpl<HomeworkMapper, Homework> implements HomeworkService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private ClassInfoMapper classInfoMapper;
    
    @Autowired
    private StudentMapper studentMapper;
    
    @Autowired
    private HomeworkSubmitMapper homeworkSubmitMapper;
    
    @Autowired
    private TeacherClassMapper teacherClassMapper;
    
    @Override
    @Transactional
    public void createHomework(HomeworkDTO homeworkDTO, Long teacherId) {
        Homework homework = new Homework();
        BeanUtils.copyProperties(homeworkDTO, homework);
        homework.setTeacherId(teacherId);
        homework.setStatus(1);
        baseMapper.insert(homework);
    }
    
    @Override
    @Transactional
    public void updateHomework(HomeworkDTO homeworkDTO) {
        Homework homework = baseMapper.selectById(homeworkDTO.getId());
        if (homework == null) {
            throw new RuntimeException("作业不存在");
        }
        BeanUtils.copyProperties(homeworkDTO, homework);
        baseMapper.updateById(homework);
    }
    
    @Override
    public HomeworkDTO getHomeworkById(Long id) {
        Homework homework = baseMapper.selectById(id);
        if (homework == null) {
            return null;
        }
        return convertToDTO(homework);
    }
    
    @Override
    public List<HomeworkDTO> getHomeworkList(Long userId, String role) {
        List<Homework> list;
        final List<Student> students = new ArrayList<>();

        if ("TEACHER".equals(role)) {
            list = baseMapper.selectByTeacherId(userId);
        } else if ("PARENT".equals(role)) {
            List<Student> studentList = studentMapper.selectByParentId(userId);
            if (studentList.isEmpty()) {
                return new ArrayList<>();
            }
            students.addAll(studentList);
            // 获取所有孩子的班级ID
            List<Long> classIds = students.stream()
                    .map(Student::getClassId)
                    .distinct()
                    .collect(Collectors.toList());
            list = baseMapper.selectByClassIds(classIds);
        } else {
            list = baseMapper.selectList(null);
        }

        final String finalRole = role;
        return list.stream().map(homework -> {
            HomeworkDTO dto = convertToDTO(homework);
            // 家长端：查询每个孩子的提交状态
            if ("PARENT".equals(finalRole) && !students.isEmpty()) {
                for (Student student : students) {
                    HomeworkSubmit submit = homeworkSubmitMapper.selectOne(
                            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<HomeworkSubmit>()
                                    .eq(HomeworkSubmit::getHomeworkId, homework.getId())
                                    .eq(HomeworkSubmit::getStudentId, student.getId())
                    );
                    if (submit != null) {
                        dto.setSubmitStatus(1); // 已提交
                        dto.setSubmitStudentName(student.getStudentName());
                        break;
                    }
                }
                if (dto.getSubmitStatus() == null) {
                    dto.setSubmitStatus(0); // 未提交
                }
            }
            return dto;
        }).collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public void deleteHomework(Long id) {
        baseMapper.deleteById(id);
    }
    
    @Override
    @Transactional
    public void submitHomework(HomeworkSubmitDTO submitDTO) {
        HomeworkSubmit submit = homeworkSubmitMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<HomeworkSubmit>()
                        .eq(HomeworkSubmit::getHomeworkId, submitDTO.getHomeworkId())
                        .eq(HomeworkSubmit::getStudentId, submitDTO.getStudentId())
        );

        if (submit == null) {
            submit = new HomeworkSubmit();
            BeanUtils.copyProperties(submitDTO, submit);
            submit.setStatus(1);
            submit.setSubmitTime(LocalDateTime.now());
            homeworkSubmitMapper.insert(submit);
        } else {
            submit.setContent(submitDTO.getContent());
            submit.setAttachmentUrl(submitDTO.getAttachmentUrl());
            submit.setSubmitTime(LocalDateTime.now());
            submit.setStatus(1);
            submit.setScore(null);
            submit.setComment(null);
            homeworkSubmitMapper.updateById(submit);
        }
    }
    
    @Override
    @Transactional
    public void gradeHomework(HomeworkSubmitDTO submitDTO) {
        HomeworkSubmit submit = homeworkSubmitMapper.selectById(submitDTO.getId());
        if (submit == null) {
            throw new RuntimeException("提交记录不存在");
        }
        submit.setScore(submitDTO.getScore());
        submit.setComment(submitDTO.getComment());
        submit.setStatus(2);
        homeworkSubmitMapper.updateById(submit);
    }
    
    @Override
    public List<HomeworkSubmitDTO> getSubmitList(Long homeworkId) {
        List<HomeworkSubmit> list = homeworkSubmitMapper.selectByHomeworkId(homeworkId);
        return list.stream().map(this::convertSubmitToDTO).collect(Collectors.toList());
    }

    @Override
    public List<HomeworkSubmitDTO> getSubmitListWithStudents(Long homeworkId) {
        // 获取作业信息
        Homework homework = baseMapper.selectById(homeworkId);
        if (homework == null) {
            return new ArrayList<>();
        }

        // 获取该班级所有学生
        List<Student> students = studentMapper.selectByClassId(homework.getClassId());

        // 获取已提交的作业
        List<HomeworkSubmit> submits = homeworkSubmitMapper.selectByHomeworkId(homeworkId);
        Map<Long, HomeworkSubmit> submitMap = submits.stream()
                .collect(Collectors.toMap(HomeworkSubmit::getStudentId, s -> s));

        // 构建所有学生的提交列表（包括未提交的）
        List<HomeworkSubmitDTO> result = new ArrayList<>();
        for (Student student : students) {
            HomeworkSubmit submit = submitMap.get(student.getId());
            HomeworkSubmitDTO dto = new HomeworkSubmitDTO();
            dto.setStudentId(student.getId());
            dto.setStudentName(student.getStudentName());

            if (submit != null) {
                dto.setId(submit.getId());
                dto.setHomeworkId(submit.getHomeworkId());
                dto.setContent(submit.getContent());
                dto.setAttachmentUrl(submit.getAttachmentUrl());
                dto.setSubmitTime(submit.getSubmitTime());
                dto.setScore(submit.getScore());
                dto.setComment(submit.getComment());
                dto.setStatus(submit.getStatus());
            }

            result.add(dto);
        }

        return result;
    }
    
    @Override
    public HomeworkSubmitDTO getSubmitByStudent(Long homeworkId, Long studentId) {
        HomeworkSubmit submit = homeworkSubmitMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<HomeworkSubmit>()
                        .eq(HomeworkSubmit::getHomeworkId, homeworkId)
                        .eq(HomeworkSubmit::getStudentId, studentId)
        );
        if (submit == null) {
            return null;
        }
        return convertSubmitToDTO(submit);
    }
    
    private HomeworkDTO convertToDTO(Homework homework) {
        HomeworkDTO dto = new HomeworkDTO();
        BeanUtils.copyProperties(homework, dto);
        
        if (homework.getTeacherId() != null) {
            User teacher = userMapper.selectById(homework.getTeacherId());
            if (teacher != null) {
                dto.setTeacherName(teacher.getRealName());
            }
        }
        
        if (homework.getClassId() != null) {
            ClassInfo classInfo = classInfoMapper.selectById(homework.getClassId());
            if (classInfo != null) {
                dto.setClassName(classInfo.getClassName());
            }
        }
        
        int totalCount = studentMapper.selectByClassId(homework.getClassId()).size();
        int submitCount = homeworkSubmitMapper.selectByHomeworkId(homework.getId()).size();
        dto.setTotalCount(totalCount);
        dto.setSubmitCount(submitCount);
        
        return dto;
    }
    
    private HomeworkSubmitDTO convertSubmitToDTO(HomeworkSubmit submit) {
        HomeworkSubmitDTO dto = new HomeworkSubmitDTO();
        BeanUtils.copyProperties(submit, dto);
        
        if (submit.getStudentId() != null) {
            Student student = studentMapper.selectById(submit.getStudentId());
            if (student != null) {
                dto.setStudentName(student.getStudentName());
            }
        }
        
        return dto;
    }
}