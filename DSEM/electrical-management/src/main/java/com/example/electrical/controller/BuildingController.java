package com.example.electrical.controller;

import com.example.electrical.common.Result;
import com.example.electrical.entity.Dormitory;
import com.example.electrical.entity.Floor;
import com.example.electrical.mapper.DormitoryMapper;
import com.example.electrical.mapper.FloorMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/building")
@RequiredArgsConstructor
public class BuildingController {

    private final FloorMapper floorMapper;
    private final DormitoryMapper dormitoryMapper;

    @GetMapping("/list")
    public Result<List<Map<String, Object>>> getBuildingList() {
        List<Long> buildingIds = floorMapper.selectAllBuildingIds();
        List<Map<String, Object>> result = buildingIds.stream().map(id -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("buildingNumber", id + "栋");
            map.put("buildingName", id + "栋");
            return map;
        }).collect(Collectors.toList());
        return Result.success(result);
    }

    @GetMapping("/{buildingId}/floor")
    public Result<List<Floor>> getFloorByBuilding(@PathVariable Long buildingId) {
        List<Floor> list = floorMapper.selectByBuildingId(buildingId);
        return Result.success(list);
    }

    @GetMapping("/floor/{floorId}/dormitory")
    public Result<List<Dormitory>> getDormitoryByFloor(@PathVariable Long floorId) {
        List<Dormitory> list = dormitoryMapper.selectByFloorId(floorId);
        return Result.success(list);
    }
}
