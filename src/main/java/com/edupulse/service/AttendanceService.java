package com.edupulse.service;

import com.edupulse.entity.AttendanceRecord;
import com.edupulse.entity.ClassSession;
import com.edupulse.entity.Student;
import com.edupulse.enums.AttendanceStatus;
import com.edupulse.exception.BadRequestException;
import com.edupulse.repository.AttendanceRecordRepository;
import com.edupulse.repository.ClassSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRecordRepository attendanceRecordRepository;
    private final ClassSessionRepository classSessionRepository;

    public Page<AttendanceRecord> getAttendance(Pageable pageable) {
        return attendanceRecordRepository.findAll(pageable);
    }

    public AttendanceRecord createAttendance(AttendanceRecord record) {
        if (record.getSession() == null || record.getStudent() == null) {
            throw new BadRequestException("Session and student are required");
        }
        if (record.getStatus() == null) {
            throw new BadRequestException("Attendance status is required");
        }
        ClassSession session = classSessionRepository.findById(record.getSession().getId())
                .orElseThrow(() -> new BadRequestException("Class session not found"));
        if (attendanceRecordRepository.findAll().stream().anyMatch(item ->
                item.getSession().getId().equals(session.getId())
                        && item.getStudent().getId().equals(record.getStudent().getId()))) {
            throw new BadRequestException("Attendance already recorded for this student and session");
        }
        return attendanceRecordRepository.save(record);
    }

    public double calculateAttendancePercentage(Student student, List<AttendanceRecord> records) {
        if (records == null || records.isEmpty()) {
            return 0.0;
        }
        long present = records.stream().filter(r -> r.getStudent().getId().equals(student.getId()) && r.getStatus() == AttendanceStatus.PRESENT).count();
        long excused = records.stream().filter(r -> r.getStudent().getId().equals(student.getId()) && r.getStatus() == AttendanceStatus.EXCUSED).count();
        long total = records.stream().filter(r -> r.getStudent().getId().equals(student.getId())).count();
        if (total == 0) return 0.0;
        return ((present + (excused * 0.5)) / (double) total) * 100.0;
    }
}
