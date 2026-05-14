package com.example.electrical.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.electrical.entity.Announcement;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AnnouncementMapper extends BaseMapper<Announcement> {
    
    @Select("SELECT * FROM announcement WHERE status = 1 AND deleted = 0 ORDER BY create_time DESC LIMIT #{limit}")
    List<Announcement> selectActiveAnnouncements(int limit);
}
