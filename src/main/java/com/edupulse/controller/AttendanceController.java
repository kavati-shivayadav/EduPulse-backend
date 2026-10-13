package com.edupulse.controller;

import com.edupulse.dto.ApiResponse;
import com.edupulse.entity.AttendanceRecord;
import com.edupulse.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @GetMapping("/attendance")
    @PreAuthorize("hasAnyAuthority('ADMIN','FACULTY','STUDENT')")
    public ResponseEntity<ApiResponse> getAttendance(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size) {
        Page<AttendanceRecord> records = attendanceService.getAttendance(PageRequest.of(page, size));
        return ResponseEntity.ok(new ApiResponse(true, "Attendance loaded", Map.of(
                "content", records.getContent().stream().map(this::toMap).toList(),
                "totalElements", records.getTotalElements()
        )));
    }

    @PostMapping("/attendance")
    @PreAuthorize("hasAnyAuthority('ADMIN','FACULTY')")
    public ResponseEntity<ApiResponse> createAttendance(@RequestBody AttendanceRecord record) {
        return ResponseEntity.ok(new ApiResponse(true, "Attendance recorded", toMap(attendanceService.createAttendance(record))));
    }

    private Map<String, Object> toMap(AttendanceRecord record) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", record.getId());
        map.put("sessionId", record.getSession() != null ? record.getSession().getId() : null);
        map.put("student", record.getStudent() != null ? record.getStudent().getStudentId() : null);
        map.put("status", record.getStatus());
        return map;
    }
}
