package com.example.electrical.service;

import com.example.electrical.common.Result;
import com.example.electrical.dto.ElectricityTrendDTO;

public interface ElectricityTrendService {
    
    /**
     * 获取用电趋势数据
     * @param studentId 学生ID
     * @param type 统计类型：day-逐日, hour-逐时段, month-逐月
     * @return 用电趋势数据
     */
    Result<ElectricityTrendDTO> getTrendData(Long studentId, String type);
}
