package com.edupulse.repository;

import com.edupulse.entity.Faculty;
import com.edupulse.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    Optional<Faculty> findByUser(User user);

    @Query("SELECT f FROM Faculty f LEFT JOIN FETCH f.user u LEFT JOIN FETCH f.department d WHERE (:search IS NULL OR :search = '' OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(f.employeeId) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Faculty> searchFaculties(String search, Pageable pageable);
}
