package com.school.communication.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.school.communication.dto.NoticeDTO;
import com.school.communication.entity.Notice;
import com.school.communication.entity.NoticeRead;
import com.school.communication.entity.Student;
import com.school.communication.entity.User;
import com.school.communication.mapper.NoticeMapper;
import com.school.communication.mapper.NoticeReadMapper;
import com.school.communication.mapper.StudentMapper;
import com.school.communication.mapper.UserMapper;
import com.school.communication.service.NoticeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private StudentMapper studentMapper;
    
    @Autowired
    private NoticeReadMapper noticeReadMapper;
    
    @Override
    @Transactional
    public void publishNotice(NoticeDTO noticeDTO, Long publisherId) {
        Notice notice = new Notice();
        BeanUtils.copyProperties(noticeDTO, notice);
        notice.setPublisherId(publisherId);
        notice.setStatus(1);
        notice.setPublishTime(LocalDateTime.now());
        baseMapper.insert(notice);
    }
    
    @Override
    @Transactional
    public void updateNotice(NoticeDTO noticeDTO) {
        Notice notice = baseMapper.selectById(noticeDTO.getId());
        if (notice == null) {
            throw new RuntimeException("通知不存在");
        }
        BeanUtils.copyProperties(noticeDTO, notice);
        baseMapper.updateById(notice);
    }
    
    @Override
    public NoticeDTO getNoticeById(Long id) {
        Notice notice = baseMapper.selectById(id);
        if (notice == null) {
            return null;
        }
        return convertToDTO(notice);
    }
    
    @Override
    public List<NoticeDTO> getNoticeList(Long userId, String role) {
        List<Notice> list;
        
        if ("ADMIN".equals(role)) {
            // 管理员查看所有通知
            list = baseMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Notice>()
                    .eq(Notice::getDeleted, 0)
                    .orderByDesc(Notice::getPriority)
                    .orderByDesc(Notice::getCreateTime)
            );
        } else if ("TEACHER".equals(role)) {
            // 教师查看发送给全部、指定班级（自己任教的班级）的通知
            list = baseMapper.selectByTargetTeacher(userId);
        } else {
            // 家长查看发送给全部、自己孩子班级、指定家长的通知
            Student student = studentMapper.selectByParentId(userId).stream().findFirst().orElse(null);
            Long classId = student != null ? student.getClassId() : null;
            list = baseMapper.selectByTargetUser(classId, userId);
        }
        
        return list.stream().map(notice -> {
            NoticeDTO dto = convertToDTO(notice);
            dto.setIsRead(noticeReadMapper.checkIsRead(notice.getId(), userId) > 0);
            return dto;
        }).collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public void deleteNotice(Long id) {
        baseMapper.deleteById(id);
    }
    
    @Override
    @Transactional
    public void readNotice(Long noticeId, Long userId) {
        if (noticeReadMapper.checkIsRead(noticeId, userId) == 0) {
            NoticeRead noticeRead = new NoticeRead();
            noticeRead.setNoticeId(noticeId);
            noticeRead.setUserId(userId);
            noticeReadMapper.insert(noticeRead);
            baseMapper.incrementReadCount(noticeId);
        }
    }
    
    @Override
    public List<NoticeDTO> getPublishedNotices(Long publisherId) {
        List<Notice> list = baseMapper.selectByPublisher(publisherId);
        return list.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    
    @Override
    public java.util.List<java.util.Map<String, Object>> getNoticeReadList(Long noticeId) {
        return noticeReadMapper.selectReadListByNoticeId(noticeId);
    }
    
    private NoticeDTO convertToDTO(Notice notice) {
        NoticeDTO dto = new NoticeDTO();
        BeanUtils.copyProperties(notice, dto);
        
        if (notice.getPublisherId() != null) {
            User user = userMapper.selectById(notice.getPublisherId());
            if (user != null) {
                dto.setPublisherName(user.getRealName());
            }
        }
        
        return dto;
    }
}