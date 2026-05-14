package com.example.electrical.service;

import com.example.electrical.common.Result;
import com.example.electrical.dto.AnnouncementDTO;
import com.example.electrical.entity.Announcement;

import java.util.List;

public interface AnnouncementService {
    
    Result<List<Announcement>> getActiveAnnouncements();
    
    Result<List<Announcement>> getAllAnnouncements();
    
    Result<Announcement> getAnnouncementDetail(Long id);
    
    Result<Void> createAnnouncement(Long publisherId, AnnouncementDTO announcementDTO);
    
    Result<Void> updateAnnouncement(Long id, AnnouncementDTO announcementDTO);
    
    Result<Void> deleteAnnouncement(Long id);
}
