package com.example.electrical.controller;

import com.example.electrical.common.Result;
import com.example.electrical.dto.RepairDTO;
import com.example.electrical.dto.RepairHandleDTO;
import com.example.electrical.entity.RepairRecord;
import com.example.electrical.service.RepairService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/repair")
@RequiredArgsConstructor
public class RepairController {

    private final RepairService repairService;

    @PostMapping("/add")
    public Result<Void> submitRepair(@Valid @RequestBody RepairDTO repairDTO, HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        if (userId == null) {
            return Result.error("请先登录");
        }
        return repairService.submitRepair(userId, repairDTO);
    }

    @GetMapping("/list")
    public Result<List<RepairRecord>> getMyRepairs(HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        if (userId == null) {
            return Result.error("请先登录");
        }
        return repairService.getMyRepairs(userId);
    }

    @GetMapping("/detail/{id}")
    public Result<RepairRecord> getRepairDetail(@PathVariable Long id) {
        return repairService.getRepairDetail(id);
    }

    @GetMapping("/pending")
    public Result<List<RepairRecord>> getPendingRepairs() {
        return repairService.getPendingRepairs();
    }

    @GetMapping("/all")
    public Result<List<RepairRecord>> getAllRepairs() {
        return repairService.getAllRepairs();
    }

    @PutMapping("/handle")
    public Result<Void> handleRepair(@Valid @RequestBody RepairHandleDTO handleDTO, HttpServletRequest request) {
        Long adminId = getUserIdFromRequest(request);
        if (adminId == null) {
            return Result.error("请先登录");
        }
        return repairService.handleRepair(adminId, handleDTO);
    }

    private Long getUserIdFromRequest(HttpServletRequest request) {
        Object userId = request.getAttribute("userId");
        if (userId == null) {
            return null;
        }
        return (Long) userId;
    }
}
