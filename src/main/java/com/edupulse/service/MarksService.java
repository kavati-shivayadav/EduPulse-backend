package com.edupulse.service;

import com.edupulse.entity.Mark;
import com.edupulse.exception.BadRequestException;
import com.edupulse.exception.ResourceNotFoundException;
import com.edupulse.repository.MarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class MarksService {

    private final MarkRepository markRepository;

    public Page<Mark> getMarks(Pageable pageable) {
        return markRepository.findAll(pageable);
    }

    public Mark createMark(Mark mark) {
        validateMark(mark);
        mark.setPercentage(calculatePercentage(mark.getMarksObtained(), mark.getMaxMarks()));
        mark.setGrade(determineGrade(mark.getPercentage()));
        return markRepository.save(mark);
    }

    public Mark updateMark(Long id, Mark updated) {
        Mark existing = markRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mark not found with id: " + id));
        validateMark(updated);
        existing.setMarksObtained(updated.getMarksObtained());
        existing.setMaxMarks(updated.getMaxMarks());
        existing.setExamination(updated.getExamination());
        existing.setSubject(updated.getSubject());
        existing.setStudent(updated.getStudent());
        existing.setExamDate(updated.getExamDate());
        existing.setPercentage(calculatePercentage(existing.getMarksObtained(), existing.getMaxMarks()));
        existing.setGrade(determineGrade(existing.getPercentage()));
        return markRepository.save(existing);
    }

    private void validateMark(Mark mark) {
        if (mark.getMarksObtained() == null || mark.getMaxMarks() == null) {
            throw new BadRequestException("Marks and max marks are required");
        }
        if (mark.getMarksObtained().compareTo(mark.getMaxMarks()) > 0) {
            throw new BadRequestException("Marks obtained cannot exceed maximum marks");
        }
    }

    private BigDecimal calculatePercentage(BigDecimal obtained, BigDecimal max) {
        if (max.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;
        return obtained.multiply(BigDecimal.valueOf(100)).divide(max, 2, RoundingMode.HALF_UP);
    }

    private String determineGrade(BigDecimal percentage) {
        if (percentage == null) return "F";
        if (percentage.compareTo(BigDecimal.valueOf(90)) >= 0) return "A";
        if (percentage.compareTo(BigDecimal.valueOf(80)) >= 0) return "B";
        if (percentage.compareTo(BigDecimal.valueOf(70)) >= 0) return "C";
        if (percentage.compareTo(BigDecimal.valueOf(60)) >= 0) return "D";
        return "F";
    }
}
