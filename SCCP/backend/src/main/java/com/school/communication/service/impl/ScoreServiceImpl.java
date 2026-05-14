package com.school.communication.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.school.communication.dto.ScoreDTO;
import com.school.communication.entity.Score;
import com.school.communication.entity.Student;
import com.school.communication.entity.User;
import com.school.communication.mapper.ScoreMapper;
import com.school.communication.mapper.StudentMapper;
import com.school.communication.mapper.UserMapper;
import com.school.communication.service.ScoreService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScoreServiceImpl extends ServiceImpl<ScoreMapper, Score> implements ScoreService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private StudentMapper studentMapper;
    
    @Override
    @Transactional
    public void createScore(ScoreDTO scoreDTO, Long teacherId) {
        Score score = new Score();
        BeanUtils.copyProperties(scoreDTO, score);
        score.setTeacherId(teacherId);
        baseMapper.insert(score);
    }
    
    @Override
    @Transactional
    public void updateScore(ScoreDTO scoreDTO) {
        Score score = baseMapper.selectById(scoreDTO.getId());
        if (score == null) {
            throw new RuntimeException("成绩记录不存在");
        }
        BeanUtils.copyProperties(scoreDTO, score);
        baseMapper.updateById(score);
    }
    
    @Override
    public ScoreDTO getScoreById(Long id) {
        Score score = baseMapper.selectById(id);
        if (score == null) {
            return null;
        }
        return convertToDTO(score);
    }
    
    @Override
    public List<ScoreDTO> getScoreList(Long userId, String role) {
        List<Score> list;
        
        if ("PARENT".equals(role)) {
            Student student = studentMapper.selectByParentId(userId).stream().findFirst().orElse(null);
            if (student == null) {
                return new ArrayList<>();
            }
            list = baseMapper.selectByStudentId(student.getId());
        } else {
            list = baseMapper.selectList(null);
        }
        
        return list.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    
    @Override
    public List<ScoreDTO> getScoreListByStudentId(Long studentId) {
        List<Score> list = baseMapper.selectByStudentId(studentId);
        return list.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    
    @Override
    public List<ScoreDTO> getScoreListByClassIdAndExamName(Long classId, String examName) {
        List<Score> list = baseMapper.selectByClassIdAndExamName(classId, examName);
        return list.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    
    @Override
    public List<String> getExamNameList() {
        return baseMapper.selectAllExamNames();
    }
    
    @Override
    @Transactional
    public void deleteScore(Long id) {
        baseMapper.deleteById(id);
    }
    
    private ScoreDTO convertToDTO(Score score) {
        ScoreDTO dto = new ScoreDTO();
        BeanUtils.copyProperties(score, dto);
        
        if (score.getStudentId() != null) {
            Student student = studentMapper.selectById(score.getStudentId());
            if (student != null) {
                dto.setStudentName(student.getStudentName());
            }
        }
        
        if (score.getTeacherId() != null) {
            User teacher = userMapper.selectById(score.getTeacherId());
            if (teacher != null) {
                dto.setTeacherName(teacher.getRealName());
            }
        }
        
        return dto;
    }
}