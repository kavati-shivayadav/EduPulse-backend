package com.edupulse.service;

import com.edupulse.entity.Mark;
import com.edupulse.entity.Student;
import com.edupulse.repository.MarkRepository;
import com.edupulse.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final StudentRepository studentRepository;
    private final MarkRepository markRepository;

    public Map<String, Object> generateStudentSummary(Long studentId) {
        Student student = studentRepository.findById(studentId).orElseThrow();
        Map<String, Object> summary = new HashMap<>();
        summary.put("student", student.getStudentId());
        summary.put("name", student.getUser().getFullName());
        summary.put("marks", markRepository.findAll().stream()
                .filter(mark -> mark.getStudent().getId().equals(studentId))
                .map(Mark::getPercentage)
                .toList());
        return summary;
    }
}
