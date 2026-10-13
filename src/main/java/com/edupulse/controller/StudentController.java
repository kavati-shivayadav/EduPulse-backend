package com.edupulse.controller;

import com.edupulse.dto.ApiResponse;
import com.edupulse.entity.Student;
import com.edupulse.service.StudentService;
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
public class StudentController {

    private final StudentService studentService;

    @GetMapping("/students")
    @PreAuthorize("hasAnyAuthority('ADMIN','FACULTY','STUDENT')")
    public ResponseEntity<ApiResponse> getStudents(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<Student> studentPage = studentService.getStudents(search, status, PageRequest.of(page, size));
        return ResponseEntity.ok(new ApiResponse(true, "Students loaded", Map.of(
                "content", studentPage.getContent().stream().map(this::toMap).toList(),
                "page", studentPage.getNumber(),
                "size", studentPage.getSize(),
                "totalElements", studentPage.getTotalElements()
        )));
    }

    @GetMapping("/students/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','FACULTY','STUDENT')")
    public ResponseEntity<ApiResponse> getStudent(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse(true, "Student loaded", toMap(studentService.getStudentById(id))));
    }

    @PostMapping("/students")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse> createStudent(@RequestBody Student student) {
        return ResponseEntity.ok(new ApiResponse(true, "Student created", toMap(studentService.createStudent(student))));
    }

    @PutMapping("/students/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse> updateStudent(@PathVariable Long id, @RequestBody Student student) {
        return ResponseEntity.ok(new ApiResponse(true, "Student updated", toMap(studentService.updateStudent(id, student))));
    }

    @DeleteMapping("/students/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok(new ApiResponse(true, "Student deleted", null));
    }

    private Map<String, Object> toMap(Student student) {
        Map<String, Object> data = new HashMap<>();
        data.put("id", student.getId());
        data.put("studentId", student.getStudentId());
        data.put("rollNumber", student.getRollNumber());
        data.put("name", student.getUser() != null ? student.getUser().getFullName() : null);
        data.put("email", student.getUser() != null ? student.getUser().getEmail() : null);
        data.put("phone", student.getPhone());
        data.put("department", student.getDepartment() != null ? student.getDepartment().getName() : null);
        data.put("semester", student.getSemester());
        data.put("status", student.getStatus());
        data.put("enrollmentDate", student.getEnrollmentDate());
        return data;
    }
}
