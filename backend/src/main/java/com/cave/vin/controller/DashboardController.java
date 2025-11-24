package com.cave.vin.controller;

import com.cave.vin.dto.DashboardDTO;
import com.cave.vin.service.DashboardService;
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

    @GetMapping
    public DashboardDTO getDashboard(
            @org.springframework.security.core.annotation.AuthenticationPrincipal org.springframework.security.oauth2.core.user.OAuth2User principal) {
        if (principal == null) {
            throw new RuntimeException("User not authenticated");
        }
        String email = principal.getAttribute("email");
        return dashboardService.getDashboardStats(email);
    }
}
