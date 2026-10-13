package com.edupulse.controller;

import com.edupulse.dto.ApiResponse;
import com.edupulse.entity.Quiz;
import com.edupulse.entity.QuizAnswer;
import com.edupulse.entity.QuizAttempt;
import com.edupulse.entity.Student;
import com.edupulse.service.QuizService;
import com.edupulse.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;
    private final StudentService studentService;

    @GetMapping("/quizzes")
    @PreAuthorize("hasAnyAuthority('ADMIN','FACULTY','STUDENT')")
    public ResponseEntity<ApiResponse> getQuizzes() {
        return ResponseEntity.ok(new ApiResponse(true, "Quizzes loaded", quizService.getQuizzes(PageRequest.of(0, 20)).getContent()));
    }

    @PostMapping("/quizzes")
    @PreAuthorize("hasAnyAuthority('ADMIN','FACULTY')")
    public ResponseEntity<ApiResponse> createQuiz(@RequestBody Quiz quiz) {
        return ResponseEntity.ok(new ApiResponse(true, "Quiz created", quizService.createQuiz(quiz)));
    }

    @GetMapping("/quizzes/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','FACULTY','STUDENT')")
    public ResponseEntity<ApiResponse> getQuiz(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse(true, "Quiz loaded", quizService.getQuiz(id)));
    }

    @PostMapping("/quizzes/{id}/attempt")
    @PreAuthorize("hasAuthority('STUDENT')")
    public ResponseEntity<ApiResponse> startAttempt(@PathVariable Long id, @RequestParam Long studentId) {
        Student student = studentService.getStudentById(studentId);
        QuizAttempt attempt = quizService.startAttempt(id, student);
        return ResponseEntity.ok(new ApiResponse(true, "Attempt started", attempt));
    }

    @PostMapping("/quizzes/attempt/{attemptId}/submit")
    @PreAuthorize("hasAuthority('STUDENT')")
    public ResponseEntity<ApiResponse> submitAttempt(@PathVariable Long attemptId, @RequestBody List<QuizAnswer> answers) {
        return ResponseEntity.ok(new ApiResponse(true, "Attempt submitted", quizService.submitAttempt(attemptId, answers)));
    }

    @GetMapping("/quizzes/{id}/questions")
    @PreAuthorize("hasAnyAuthority('ADMIN','FACULTY','STUDENT')")
    public ResponseEntity<ApiResponse> getQuestions(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse(true, "Questions loaded", quizService.getQuestions(id)));
    }
}
