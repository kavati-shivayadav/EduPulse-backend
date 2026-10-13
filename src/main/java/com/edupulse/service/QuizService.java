package com.edupulse.service;

import com.edupulse.entity.Quiz;
import com.edupulse.entity.QuizAnswer;
import com.edupulse.entity.QuizAttempt;
import com.edupulse.entity.QuizQuestion;
import com.edupulse.entity.Student;
import com.edupulse.exception.BadRequestException;
import com.edupulse.exception.ResourceNotFoundException;
import com.edupulse.repository.QuizAnswerRepository;
import com.edupulse.repository.QuizAttemptRepository;
import com.edupulse.repository.QuizQuestionRepository;
import com.edupulse.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuizQuestionRepository quizQuestionRepository;
    private final QuizAttemptRepository quizAttemptRepository;
    private final QuizAnswerRepository quizAnswerRepository;

    public Page<Quiz> getQuizzes(Pageable pageable) {
        return quizRepository.findAll(pageable);
    }

    public Quiz createQuiz(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    public Quiz getQuiz(Long id) {
        return quizRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found"));
    }

    public List<QuizQuestion> getQuestions(Long quizId) {
        return quizQuestionRepository.findAll().stream()
                .filter(question -> question.getQuiz().getId().equals(quizId))
                .toList();
    }

    public QuizAttempt startAttempt(Long quizId, Student student) {
        Quiz quiz = getQuiz(quizId);
        long attempts = quizAttemptRepository.findAll().stream()
                .filter(attempt -> attempt.getQuiz().getId().equals(quizId) && attempt.getStudent().getId().equals(student.getId()))
                .count();
        if (quiz.getMaxAttempts() != null && attempts >= quiz.getMaxAttempts()) {
            throw new BadRequestException("Maximum attempts reached for this quiz");
        }
        QuizAttempt attempt = new QuizAttempt();
        attempt.setQuiz(quiz);
        attempt.setStudent(student);
        attempt.setStartedAt(Instant.now());
        attempt.setTotalMarks(BigDecimal.valueOf(100));
        attempt.setStatus("IN_PROGRESS");
        return quizAttemptRepository.save(attempt);
    }

    public QuizAttempt submitAttempt(Long attemptId, List<QuizAnswer> answers) {
        QuizAttempt attempt = quizAttemptRepository.findById(attemptId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz attempt not found"));
        int correct = 0;
        for (QuizAnswer answer : answers) {
            answer.setAttempt(attempt);
            if (answer.isCorrect()) {
                correct++;
            }
            quizAnswerRepository.save(answer);
        }
        BigDecimal score = BigDecimal.valueOf(correct * 10L);
        attempt.setScore(score);
        attempt.setSubmittedAt(Instant.now());
        attempt.setStatus("SUBMITTED");
        return quizAttemptRepository.save(attempt);
    }
}
