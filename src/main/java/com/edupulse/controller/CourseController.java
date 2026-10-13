package com.edupulse.controller;

import com.edupulse.dto.ApiResponse;
import com.edupulse.entity.Course;
import com.edupulse.entity.Subject;
import com.edupulse.service.CourseService;
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
public class CourseController {

    private final CourseService courseService;

    @GetMapping("/courses")
    @PreAuthorize("hasAnyAuthority('ADMIN','FACULTY','STUDENT')")
    public ResponseEntity<ApiResponse> getCourses(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") int size) {
        Page<Course> courses = courseService.getCourses(PageRequest.of(page, size));
        return ResponseEntity.ok(new ApiResponse(true, "Courses loaded", Map.of(
                "content", courses.getContent().stream().map(this::toCourseMap).toList(),
                "totalElements", courses.getTotalElements()
        )));
    }

    @PostMapping("/courses")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse> createCourse(@RequestBody Course course) {
        return ResponseEntity.ok(new ApiResponse(true, "Course created", toCourseMap(courseService.createCourse(course))));
    }

    @GetMapping("/subjects")
    @PreAuthorize("hasAnyAuthority('ADMIN','FACULTY','STUDENT')")
    public ResponseEntity<ApiResponse> getSubjects(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") int size) {
        Page<Subject> subjects = courseService.getSubjects(PageRequest.of(page, size));
        return ResponseEntity.ok(new ApiResponse(true, "Subjects loaded", Map.of(
                "content", subjects.getContent().stream().map(this::toSubjectMap).toList(),
                "totalElements", subjects.getTotalElements()
        )));
    }

    @PostMapping("/subjects")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse> createSubject(@RequestBody Subject subject) {
        return ResponseEntity.ok(new ApiResponse(true, "Subject created", toSubjectMap(courseService.saveSubject(subject))));
    }

    private Map<String, Object> toCourseMap(Course course) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", course.getId());
        map.put("code", course.getCode());
        map.put("title", course.getTitle());
        map.put("credits", course.getCredits());
        map.put("description", course.getDescription());
        map.put("department", course.getDepartment() != null ? course.getDepartment().getName() : null);
        return map;
    }

    private Map<String, Object> toSubjectMap(Subject subject) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", subject.getId());
        map.put("code", subject.getCode());
        map.put("name", subject.getName());
        map.put("credits", subject.getCredits());
        map.put("description", subject.getDescription());
        map.put("course", subject.getCourse() != null ? subject.getCourse().getTitle() : null);
        return map;
    }
}
