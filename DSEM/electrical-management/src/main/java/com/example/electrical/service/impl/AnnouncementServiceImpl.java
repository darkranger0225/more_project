package com.example.electrical.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.electrical.common.Constants;
import com.example.electrical.common.Result;
import com.example.electrical.dto.AnnouncementDTO;
import com.example.electrical.entity.Announcement;
import com.example.electrical.mapper.AnnouncementMapper;
import com.example.electrical.service.AnnouncementService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnnouncementServiceImpl extends ServiceImpl<AnnouncementMapper, Announcement> implements AnnouncementService {

    @Autowired
    private AnnouncementMapper announcementMapper;

    @Override
    public Result<List<Announcement>> getActiveAnnouncements() {
        List<Announcement> list = announcementMapper.selectActiveAnnouncements(10);
        return Result.success(list);
    }

    @Override
    public Result<List<Announcement>> getAllAnnouncements() {
        List<Announcement> list = announcementMapper.selectList(null);
        return Result.success(list);
    }

    @Override
    public Result<Announcement> getAnnouncementDetail(Long id) {
        Announcement announcement = announcementMapper.selectById(id);
        if (announcement == null) {
            return Result.error("公告不存在");
        }
        return Result.success(announcement);
    }

    @Override
    public Result<Void> createAnnouncement(Long publisherId, AnnouncementDTO announcementDTO) {
        Announcement announcement = new Announcement();
        BeanUtils.copyProperties(announcementDTO, announcement);
        announcement.setPublisherId(publisherId);
        announcement.setStatus(Constants.ANNOUNCEMENT_STATUS_ENABLED);
        
        announcementMapper.insert(announcement);
        return Result.success();
    }

    @Override
    public Result<Void> updateAnnouncement(Long id, AnnouncementDTO announcementDTO) {
        Announcement announcement = announcementMapper.selectById(id);
        if (announcement == null) {
            return Result.error("公告不存在");
        }
        
        announcement.setTitle(announcementDTO.getTitle());
        announcement.setContent(announcementDTO.getContent());
        announcement.setImage(announcementDTO.getImage());
        
        announcementMapper.updateById(announcement);
        return Result.success();
    }

    @Override
    public Result<Void> deleteAnnouncement(Long id) {
        announcementMapper.deleteById(id);
        return Result.success();
    }
}
