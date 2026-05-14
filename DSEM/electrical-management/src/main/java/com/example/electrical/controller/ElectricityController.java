package com.example.electrical.controller;

import com.example.electrical.common.Result;
import com.example.electrical.dto.ElectricityTrendDTO;
import com.example.electrical.dto.ElectricityUsageDTO;
import com.example.electrical.entity.ElectricityUsage;
import com.example.electrical.service.ElectricityService;
import com.example.electrical.service.ElectricityTrendService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/electricity")
@RequiredArgsConstructor
public class ElectricityController {

    private final ElectricityService electricityService;
    private final ElectricityTrendService electricityTrendService;

    @GetMapping("/list")
    public Result<List<ElectricityUsage>> getUsageList(HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        if (userId == null) {
            return Result.error("请先登录");
        }
        return electricityService.getUsageList(userId);
    }

    @GetMapping("/balance")
    public Result<BigDecimal> getBalance(HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        if (userId == null) {
            return Result.error("请先登录");
        }
        return electricityService.getBalance(userId);
    }

    @GetMapping("/unpaid")
    public Result<List<ElectricityUsage>> getUnpaidList(HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        if (userId == null) {
            return Result.error("请先登录");
        }
        return electricityService.getUnpaidList(userId);
    }

    @PostMapping("/add")
    public Result<Void> addUsage(@Valid @RequestBody ElectricityUsageDTO usageDTO, HttpServletRequest request) {
        Long adminId = getUserIdFromRequest(request);
        if (adminId == null) {
            return Result.error("请先登录");
        }
        return electricityService.addUsage(adminId, usageDTO);
    }

    @GetMapping("/floor/{floorId}")
    public Result<List<ElectricityUsage>> getUsageByFloor(@PathVariable Long floorId) {
        return electricityService.getUsageByFloor(floorId);
    }

    @GetMapping("/trend")
    public Result<ElectricityTrendDTO> getTrendData(
            @RequestParam(defaultValue = "day") String type,
            HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        if (userId == null) {
            return Result.error("请先登录");
        }
        return electricityTrendService.getTrendData(userId, type);
    }

    private Long getUserIdFromRequest(HttpServletRequest request) {
        Object userId = request.getAttribute("userId");
        if (userId == null) {
            return null;
        }
        return (Long) userId;
    }
}
