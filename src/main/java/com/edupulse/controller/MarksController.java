package com.edupulse.controller;

import com.edupulse.dto.ApiResponse;
import com.edupulse.entity.Mark;
import com.edupulse.service.MarksService;
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
public class MarksController {

    private final MarksService marksService;

    @GetMapping("/marks")
    @PreAuthorize("hasAnyAuthority('ADMIN','FACULTY','STUDENT')")
    public ResponseEntity<ApiResponse> getMarks(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size) {
        Page<Mark> marks = marksService.getMarks(PageRequest.of(page, size));
        return ResponseEntity.ok(new ApiResponse(true, "Marks loaded", Map.of(
                "content", marks.getContent().stream().map(this::toMap).toList(),
                "totalElements", marks.getTotalElements()
        )));
    }

    @PostMapping("/marks")
    @PreAuthorize("hasAnyAuthority('ADMIN','FACULTY')")
    public ResponseEntity<ApiResponse> createMark(@RequestBody Mark mark) {
        return ResponseEntity.ok(new ApiResponse(true, "Mark created", toMap(marksService.createMark(mark))));
    }

    @PutMapping("/marks/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','FACULTY')")
    public ResponseEntity<ApiResponse> updateMark(@PathVariable Long id, @RequestBody Mark mark) {
        return ResponseEntity.ok(new ApiResponse(true, "Mark updated", toMap(marksService.updateMark(id, mark))));
    }

    private Map<String, Object> toMap(Mark mark) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", mark.getId());
        map.put("student", mark.getStudent() != null ? mark.getStudent().getStudentId() : null);
        map.put("subject", mark.getSubject() != null ? mark.getSubject().getName() : null);
        map.put("marksObtained", mark.getMarksObtained());
        map.put("maxMarks", mark.getMaxMarks());
        map.put("percentage", mark.getPercentage());
        map.put("grade", mark.getGrade());
        return map;
    }
}
