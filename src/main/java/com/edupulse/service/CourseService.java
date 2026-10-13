package com.edupulse.service;

import com.edupulse.entity.Course;
import com.edupulse.entity.Subject;
import com.edupulse.exception.BadRequestException;
import com.edupulse.exception.ResourceNotFoundException;
import com.edupulse.repository.CourseRepository;
import com.edupulse.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final SubjectRepository subjectRepository;

    public Page<Course> getCourses(Pageable pageable) {
        return courseRepository.findAll(pageable);
    }

    public Course createCourse(Course course) {
        if (courseRepository.findByCode(course.getCode()).isPresent()) {
            throw new BadRequestException("Course with code already exists");
        }
        return courseRepository.save(course);
    }

    public Course getCourse(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
    }

    public Page<Subject> getSubjects(Pageable pageable) {
        return subjectRepository.findAll(pageable);
    }

    public Subject saveSubject(Subject subject) {
        if (subjectRepository.findByCode(subject.getCode()).isPresent()) {
            throw new BadRequestException("Subject with code already exists");
        }
        return subjectRepository.save(subject);
    }
}
