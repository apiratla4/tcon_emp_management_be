package com.tcon.empManagement.Service;

import com.tcon.empManagement.Dto.AttendanceCreateRequest;
import com.tcon.empManagement.Dto.AttendanceResponse;
import com.tcon.empManagement.Dto.AttendanceUpdateRequest;
import com.tcon.empManagement.Entity.Attendance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceService {

    AttendanceResponse checkIn(AttendanceCreateRequest request);

    AttendanceResponse checkOut(String id, AttendanceUpdateRequest request);

    Page<AttendanceResponse> getAllAttendance(Pageable pageable);

    List<AttendanceResponse> getAttendanceByEmployee(String empId);

    AttendanceResponse getById(String id);

    void deleteById(String id);

    Optional<Attendance> findByEmpIdAndDate(String empId, LocalDate date);

    Attendance save(Attendance attendance);

    List<AttendanceResponse> getAttendanceByDate(LocalDate date);

    List<AttendanceResponse> getWeeklyTimesheet(String empId, LocalDate weekStart);
}
