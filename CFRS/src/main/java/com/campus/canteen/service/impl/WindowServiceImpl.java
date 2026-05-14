package com.campus.canteen.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.canteen.entity.Window;
import com.campus.canteen.mapper.WindowMapper;
import com.campus.canteen.service.WindowService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WindowServiceImpl extends ServiceImpl<WindowMapper, Window> implements WindowService {
    
    @Override
    public List<Window> getByFloor(Integer floor) {
        return baseMapper.selectByFloor(floor);
    }
    
    @Override
    public List<Window> getOpenWindows() {
        return baseMapper.selectOpenWindows();
    }
    
    @Override
    public boolean updateStatus(Long windowId, Integer status) {
        Window window = getById(windowId);
        if (window == null) {
            return false;
        }
        window.setStatus(status);
        return updateById(window);
    }
}
