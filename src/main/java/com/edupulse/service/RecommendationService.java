package com.edupulse.service;

import com.edupulse.entity.Mark;
import com.edupulse.entity.Recommendation;
import com.edupulse.entity.Student;
import com.edupulse.repository.MarkRepository;
import com.edupulse.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final MarkRepository markRepository;
    private final RecommendationRepository recommendationRepository;

    public List<Recommendation> generateForStudent(Student student) {
        List<Recommendation> recommendations = new ArrayList<>();
        List<Mark> marks = markRepository.findAll().stream()
                .filter(mark -> mark.getStudent().getId().equals(student.getId()))
                .toList();

        for (Mark mark : marks) {
            if (mark.getPercentage() != null && mark.getPercentage().compareTo(BigDecimal.valueOf(60)) < 0) {
                Recommendation recommendation = new Recommendation();
                recommendation.setStudent(student);
                recommendation.setCategory("Weak Subject");
                recommendation.setTitle("Focus on " + mark.getSubject().getName());
                recommendation.setDescription("Your current score is below target. Review class notes and complete extra practice in this subject.");
                recommendation.setPriority("HIGH");
                recommendations.add(recommendation);
            }
        }

        if (recommendations.isEmpty()) {
            Recommendation recommendation = new Recommendation();
            recommendation.setStudent(student);
            recommendation.setCategory("Performance");
            recommendation.setTitle("Maintain consistent performance");
            recommendation.setDescription("Your performance is stable, so continue to revise weekly and maintain good attendance.");
            recommendation.setPriority("MEDIUM");
            recommendations.add(recommendation);
        }

        return recommendationRepository.saveAll(recommendations);
    }
}
