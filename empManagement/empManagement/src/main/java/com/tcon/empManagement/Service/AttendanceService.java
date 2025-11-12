package com.tcon.empManagement.Service;


import com.tcon.empManagement.Dto.AttendanceCreateRequest;
import com.tcon.empManagement.Dto.AttendanceResponse;
import com.tcon.empManagement.Dto.AttendanceUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AttendanceService {

    // Check-in: create attendance record for the day
    AttendanceResponse checkIn(AttendanceCreateRequest request);

    // Check-out: update attendance record with check-out time and calculate work hours
    AttendanceResponse checkOut(String id, AttendanceUpdateRequest request);

    // Get all attendance (paginated) - Manager/HR/Owner
    Page<AttendanceResponse> getAllAttendance(Pageable pageable);

    // Get attendance by employeeId - Employee
    List<AttendanceResponse> getAttendanceByEmployee(String empId);

    // Get by id
    AttendanceResponse getById(String id);

    // Delete by id (optional admin operation)
    void deleteById(String id);
}
