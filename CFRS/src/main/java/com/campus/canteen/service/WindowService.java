package com.campus.canteen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.canteen.entity.Window;

import java.util.List;

public interface WindowService extends IService<Window> {
    
    List<Window> getByFloor(Integer floor);
    
    List<Window> getOpenWindows();
    
    boolean updateStatus(Long windowId, Integer status);
}
