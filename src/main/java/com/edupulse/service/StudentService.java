package com.edupulse.service;

import com.edupulse.entity.Student;
import com.edupulse.exception.BadRequestException;
import com.edupulse.exception.ResourceNotFoundException;
import com.edupulse.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public Page<Student> getStudents(String search, String status, Pageable pageable) {
        return studentRepository.searchStudents(search, status, pageable);
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
    }

    public Student createStudent(Student student) {
        if (student.getStudentId() == null || student.getStudentId().isBlank()) {
            throw new BadRequestException("Student ID is required");
        }
        return studentRepository.save(student);
    }

    public Student updateStudent(Long id, Student patch) {
        Student existing = getStudentById(id);
        if (patch.getStudentId() != null) existing.setStudentId(patch.getStudentId());
        if (patch.getRollNumber() != null) existing.setRollNumber(patch.getRollNumber());
        if (patch.getPhone() != null) existing.setPhone(patch.getPhone());
        if (patch.getDepartment() != null) existing.setDepartment(patch.getDepartment());
        if (patch.getSemester() != null) existing.setSemester(patch.getSemester());
        if (patch.getStatus() != null) existing.setStatus(patch.getStatus());
        if (patch.getEnrollmentDate() != null) existing.setEnrollmentDate(patch.getEnrollmentDate());
        return studentRepository.save(existing);
    }

    public void deleteStudent(Long id) {
        Student student = getStudentById(id);
        studentRepository.delete(student);
    }
}
