package com.edupulse.config;

import com.edupulse.entity.*;
import com.edupulse.enums.Role;
import com.edupulse.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Profile({"dev", "local"})
@RequiredArgsConstructor
public class DataSeeder implements ApplicationRunner {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;
    private final DepartmentRepository departmentRepository;
    private final CourseRepository courseRepository;
    private final SubjectRepository subjectRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        if (userRepository.count() > 0) {
            return;
        }

        Department department = new Department();
        department.setName("Computer Science");
        department.setCode("CSE");
        department.setDescription("Core computing and software engineering");
        department = departmentRepository.save(department);

        User adminUser = createUser("admin", "admin@edupulse.local", "Admin@123", "System Admin", Role.ADMIN);
        User facultyUser = createUser("faculty", "faculty@edupulse.local", "Faculty@123", "Jane Faculty", Role.FACULTY);
        User studentUser = createUser("student", "student@edupulse.local", "Student@123", "John Student", Role.STUDENT);

        Faculty faculty = new Faculty();
        faculty.setUser(facultyUser);
        faculty.setEmployeeId("FAC-1001");
        faculty.setDepartment(department);
        faculty.setDesignation("Assistant Professor");
        facultyRepository.save(faculty);

        Student student = new Student();
        student.setUser(studentUser);
        student.setStudentId("STU-2001");
        student.setRollNumber("CS-21-001");
        student.setPhone("+1-555-222-1111");
        student.setDepartment(department);
        student.setSemester(6);
        student.setStatus("ACTIVE");
        student.setEnrollmentDate(LocalDate.of(2021, 8, 15));
        studentRepository.save(student);

        Course course = new Course();
        course.setCode("CS101");
        course.setTitle("Data Structures");
        course.setCredits(4);
        course.setDepartment(department);
        course.setDescription("Core data structures and algorithms");
        courseRepository.save(course);

        Subject subject = new Subject();
        subject.setCode("DS-101");
        subject.setName("Data Structures");
        subject.setCourse(course);
        subject.setCredits(4);
        subject.setDescription("Algorithms and problem solving");
        subjectRepository.save(subject);
    }

    private User createUser(String username, String email, String rawPassword, String fullName, Role role) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setFullName(fullName);
        user.setRole(role);
        user.setEnabled(true);
        return userRepository.save(user);
    }
}
