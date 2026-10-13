package com.edupulse.service;

import com.edupulse.entity.Mark;
import com.edupulse.entity.Student;
import com.edupulse.repository.MarkRepository;
import com.edupulse.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final StudentRepository studentRepository;
    private final MarkRepository markRepository;

    public Map<String, Object> getOverview() {
        long totalStudents = studentRepository.count();
        List<Mark> marks = markRepository.findAll();
        BigDecimal avg = marks.stream()
                .map(Mark::getPercentage)
                .filter(java.util.Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(Math.max(1, marks.size())), 2, RoundingMode.HALF_UP);

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalStudents", totalStudents);
        stats.put("averageMarks", avg);
        stats.put("marksCount", marks.size());
        stats.put("passRate", calculatePassRate(marks));
        return stats;
    }

    private BigDecimal calculatePassRate(List<Mark> marks) {
        if (marks.isEmpty()) return BigDecimal.ZERO;
        long pass = marks.stream()
                .filter(mark -> mark.getPercentage() != null && mark.getPercentage().compareTo(BigDecimal.valueOf(40)) >= 0)
                .count();
        return BigDecimal.valueOf(pass * 100L).divide(BigDecimal.valueOf(marks.size()), 2, RoundingMode.HALF_UP);
    }
}
