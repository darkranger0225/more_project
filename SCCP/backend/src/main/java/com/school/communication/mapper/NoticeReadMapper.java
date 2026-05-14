package com.school.communication.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.school.communication.entity.NoticeRead;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface NoticeReadMapper extends BaseMapper<NoticeRead> {
    
    @Select("SELECT COUNT(*) FROM notice_read WHERE notice_id = #{noticeId} AND user_id = #{userId}")
    int checkIsRead(@Param("noticeId") Long noticeId, @Param("userId") Long userId);
    
    @Select("SELECT nr.*, u.real_name as user_name, u.role as user_role " +
            "FROM notice_read nr " +
            "LEFT JOIN sys_user u ON nr.user_id = u.id " +
            "WHERE nr.notice_id = #{noticeId} " +
            "ORDER BY nr.read_time DESC")
    java.util.List<java.util.Map<String, Object>> selectReadListByNoticeId(@Param("noticeId") Long noticeId);
}