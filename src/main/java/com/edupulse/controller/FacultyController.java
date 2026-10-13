package com.edupulse.controller;

import com.edupulse.dto.ApiResponse;
import com.edupulse.entity.Faculty;
import com.edupulse.service.FacultyService;
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
public class FacultyController {

    private final FacultyService facultyService;

    @GetMapping("/faculty")
    @PreAuthorize("hasAnyAuthority('ADMIN','FACULTY')")
    public ResponseEntity<ApiResponse> listFaculty(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<Faculty> facultyPage = facultyService.getFaculties(search, PageRequest.of(page, size));
        return ResponseEntity.ok(new ApiResponse(true, "Faculty loaded", Map.of(
                "content", facultyPage.getContent().stream().map(this::toMap).toList(),
                "page", facultyPage.getNumber(),
                "totalElements", facultyPage.getTotalElements()
        )));
    }

    @GetMapping("/faculty/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','FACULTY')")
    public ResponseEntity<ApiResponse> getFaculty(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse(true, "Faculty loaded", toMap(facultyService.getById(id))));
    }

    @PostMapping("/faculty")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse> createFaculty(@RequestBody Faculty faculty) {
        return ResponseEntity.ok(new ApiResponse(true, "Faculty created", toMap(facultyService.save(faculty))));
    }

    @PutMapping("/faculty/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse> updateFaculty(@PathVariable Long id, @RequestBody Faculty faculty) {
        return ResponseEntity.ok(new ApiResponse(true, "Faculty updated", toMap(facultyService.update(id, faculty))));
    }

    private Map<String, Object> toMap(Faculty faculty) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", faculty.getId());
        map.put("employeeId", faculty.getEmployeeId());
        map.put("name", faculty.getUser() != null ? faculty.getUser().getFullName() : null);
        map.put("email", faculty.getUser() != null ? faculty.getUser().getEmail() : null);
        map.put("phone", faculty.getPhone());
        map.put("department", faculty.getDepartment() != null ? faculty.getDepartment().getName() : null);
        map.put("designation", faculty.getDesignation());
        return map;
    }
}
