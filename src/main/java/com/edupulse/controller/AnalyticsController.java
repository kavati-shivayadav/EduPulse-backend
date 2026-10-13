package com.edupulse.controller;

import com.edupulse.dto.ApiResponse;
import com.edupulse.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/analytics/overview")
    @PreAuthorize("hasAnyAuthority('ADMIN','FACULTY','STUDENT')")
    public ResponseEntity<ApiResponse> overview() {
        return ResponseEntity.ok(new ApiResponse(true, "Analytics overview loaded", analyticsService.getOverview()));
    }
}
