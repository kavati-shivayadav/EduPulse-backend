package com.edupulse.controller;

import com.edupulse.dto.ApiResponse;
import com.edupulse.entity.Recommendation;
import com.edupulse.entity.Student;
import com.edupulse.service.RecommendationService;
import com.edupulse.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;
    private final StudentService studentService;

    @GetMapping("/recommendations")
    @PreAuthorize("hasAnyAuthority('ADMIN','FACULTY','STUDENT')")
    public ResponseEntity<ApiResponse> recommendations(@RequestParam Long studentId) {
        Student student = studentService.getStudentById(studentId);
        List<Recommendation> recommendations = recommendationService.generateForStudent(student);
        return ResponseEntity.ok(new ApiResponse(true, "Recommendations generated", recommendations));
    }
}
