package com.edupulse.repository;

import com.edupulse.entity.Student;
import com.edupulse.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByUser(User user);

    @Query("SELECT s FROM Student s LEFT JOIN FETCH s.user u LEFT JOIN FETCH s.department d WHERE (:search IS NULL OR :search = '' OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(s.studentId) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(s.rollNumber) LIKE LOWER(CONCAT('%', :search, '%'))) AND (:status IS NULL OR :status = '' OR s.status = :status)")
    Page<Student> searchStudents(String search, String status, Pageable pageable);
}
