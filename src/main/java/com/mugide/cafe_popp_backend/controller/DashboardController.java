package com.mugide.cafe_popp_backend.controller;

import com.mugide.cafe_popp_backend.dto.DashboardDto;
import com.mugide.cafe_popp_backend.service.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/summary")
    public DashboardDto getSummary() {
        return dashboardService.getSummary();
    }
}