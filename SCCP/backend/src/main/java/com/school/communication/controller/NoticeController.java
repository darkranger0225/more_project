package com.school.communication.controller;

import com.school.communication.dto.NoticeDTO;
import com.school.communication.dto.Result;
import com.school.communication.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/notice")
public class NoticeController {
    
    @Autowired
    private NoticeService noticeService;
    
    @GetMapping("/list")
    public Result<List<NoticeDTO>> getNoticeList(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        String role = (String) request.getAttribute("role");
        List<NoticeDTO> list = noticeService.getNoticeList(userId, role);
        return Result.success(list);
    }
    
    @GetMapping("/{id}")
    public Result<NoticeDTO> getNoticeById(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        NoticeDTO notice = noticeService.getNoticeById(id);
        
        if (notice != null && !"ADMIN".equals(request.getAttribute("role"))) {
            noticeService.readNotice(id, userId);
        }
        
        return Result.success(notice);
    }
    
    @PostMapping
    public Result<Void> createNotice(@Validated @RequestBody NoticeDTO noticeDTO, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        noticeService.publishNotice(noticeDTO, userId);
        return Result.success();
    }
    
    @PutMapping
    public Result<Void> updateNotice(@Validated @RequestBody NoticeDTO noticeDTO) {
        noticeService.updateNotice(noticeDTO);
        return Result.success();
    }
    
    @DeleteMapping("/{id}")
    public Result<Void> deleteNotice(@PathVariable Long id) {
        noticeService.deleteNotice(id);
        return Result.success();
    }
    
    @PostMapping("/{id}/read")
    public Result<Void> readNotice(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        noticeService.readNotice(id, userId);
        return Result.success();
    }
    
    @GetMapping("/published")
    public Result<List<NoticeDTO>> getPublishedNotices(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        List<NoticeDTO> list = noticeService.getPublishedNotices(userId);
        return Result.success(list);
    }
    
    @GetMapping("/{id}/read-list")
    public Result<java.util.List<java.util.Map<String, Object>>> getNoticeReadList(@PathVariable Long id) {
        java.util.List<java.util.Map<String, Object>> list = noticeService.getNoticeReadList(id);
        return Result.success(list);
    }
}