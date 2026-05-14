package com.example.electrical.controller;

import com.example.electrical.common.Result;
import com.example.electrical.dto.AnnouncementDTO;
import com.example.electrical.entity.Announcement;
import com.example.electrical.service.AnnouncementService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/announcement")
@RequiredArgsConstructor
public class AnnouncementController {

    private final AnnouncementService announcementService;

    @GetMapping("/list")
    public Result<List<Announcement>> getActiveAnnouncements() {
        return announcementService.getActiveAnnouncements();
    }

    @GetMapping("/all")
    public Result<List<Announcement>> getAllAnnouncements() {
        return announcementService.getAllAnnouncements();
    }

    @GetMapping("/detail/{id}")
    public Result<Announcement> getAnnouncementDetail(@PathVariable Long id) {
        return announcementService.getAnnouncementDetail(id);
    }

    @PostMapping("/create")
    public Result<Void> createAnnouncement(@Valid @RequestBody AnnouncementDTO announcementDTO, HttpServletRequest request) {
        Long publisherId = getUserIdFromRequest(request);
        if (publisherId == null) {
            return Result.error("请先登录");
        }
        return announcementService.createAnnouncement(publisherId, announcementDTO);
    }

    @PutMapping("/update/{id}")
    public Result<Void> updateAnnouncement(@PathVariable Long id, @Valid @RequestBody AnnouncementDTO announcementDTO) {
        return announcementService.updateAnnouncement(id, announcementDTO);
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteAnnouncement(@PathVariable Long id) {
        return announcementService.deleteAnnouncement(id);
    }

    private Long getUserIdFromRequest(HttpServletRequest request) {
        Object userId = request.getAttribute("userId");
        if (userId == null) {
            return null;
        }
        return (Long) userId;
    }
}
