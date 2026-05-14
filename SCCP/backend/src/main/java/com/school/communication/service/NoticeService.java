package com.school.communication.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.school.communication.dto.NoticeDTO;
import com.school.communication.entity.Notice;

import java.util.List;

public interface NoticeService extends IService<Notice> {
    
    void publishNotice(NoticeDTO noticeDTO, Long publisherId);
    
    void updateNotice(NoticeDTO noticeDTO);
    
    NoticeDTO getNoticeById(Long id);
    
    List<NoticeDTO> getNoticeList(Long userId, String role);
    
    void deleteNotice(Long id);
    
    void readNotice(Long noticeId, Long userId);
    
    List<NoticeDTO> getPublishedNotices(Long publisherId);
    
    java.util.List<java.util.Map<String, Object>> getNoticeReadList(Long noticeId);
}