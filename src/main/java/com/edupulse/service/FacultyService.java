package com.edupulse.service;

import com.edupulse.entity.Faculty;
import com.edupulse.exception.ResourceNotFoundException;
import com.edupulse.repository.FacultyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FacultyService {

    private final FacultyRepository facultyRepository;

    public Page<Faculty> getFaculties(String search, Pageable pageable) {
        return facultyRepository.searchFaculties(search, pageable);
    }

    public Faculty getById(Long id) {
        return facultyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty not found with id: " + id));
    }

    public Faculty save(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Faculty update(Long id, Faculty patch) {
        Faculty faculty = getById(id);
        if (patch.getEmployeeId() != null) faculty.setEmployeeId(patch.getEmployeeId());
        if (patch.getPhone() != null) faculty.setPhone(patch.getPhone());
        if (patch.getDepartment() != null) faculty.setDepartment(patch.getDepartment());
        if (patch.getDesignation() != null) faculty.setDesignation(patch.getDesignation());
        return facultyRepository.save(faculty);
    }
}
