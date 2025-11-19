package com.tcon.empManagement.Controller;

import com.tcon.empManagement.Dto.AttendanceCreateRequest;
import com.tcon.empManagement.Dto.AttendanceResponse;
import com.tcon.empManagement.Dto.AttendanceUpdateRequest;
import com.tcon.empManagement.Service.AttendanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
@Slf4j
public class AttendanceController {

    private final AttendanceService service;

    // 1. Get all attendance records (paginated)
    @GetMapping
    public Page<AttendanceResponse> getAllAttendance(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("GET /api/attendance?page={}&size={}", page, size);
        try {
            return service.getAllAttendance(PageRequest.of(page, size));
        } catch (Exception ex) {
            log.error("GET /api/attendance failed", ex);
            throw ex;
        }
    }

    // 2. Get attendance for a specific employee
    @GetMapping("/employee/{empId}")
    public List<AttendanceResponse> getAttendanceByEmployee(@PathVariable String empId) {
        log.info("GET /api/attendance/employee/{}", empId);
        try {
            return service.getAttendanceByEmployee(empId);
        } catch (Exception ex) {
            log.error("GET /api/attendance/employee/{} failed", empId, ex);
            throw ex;
        }
    }

    // 3. Check-in endpoint
    @PostMapping("/checkin")
    public ResponseEntity<AttendanceResponse> checkIn(@Valid @RequestBody AttendanceCreateRequest req) {
        log.info("POST /api/attendance/checkin empId={} date={}", req.getEmpId(), req.getDate());
        try {
            AttendanceResponse res = service.checkIn(req);
            return ResponseEntity.created(URI.create("/api/attendance/" + res.getId())).body(res);
        } catch (Exception ex) {
            log.error("POST /api/attendance/checkin failed empId={}", req.getEmpId(), ex);
            throw ex;
        }
    }

    // 4. Check-out endpoint
    @PatchMapping("/checkout/{id}")
    public AttendanceResponse checkOut(@PathVariable String id,
                                       @Valid @RequestBody AttendanceUpdateRequest req) {
        log.info("PATCH /api/attendance/checkout/{}", id);
        try {
            return service.checkOut(id, req);
        } catch (Exception ex) {
            log.error("PATCH /api/attendance/checkout/{} failed", id, ex);
            throw ex;
        }
    }

    // 5. Get by attendance record id
    @GetMapping("/{id}")
    public AttendanceResponse getById(@PathVariable String id) {
        log.info("GET /api/attendance/{}", id);
        try {
            return service.getById(id);
        } catch (Exception ex) {
            log.error("GET /api/attendance/{} failed", id, ex);
            throw ex;
        }
    }

    // 6. Delete by id (admin only)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        log.info("DELETE /api/attendance/{}", id);
        try {
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception ex) {
            log.error("DELETE /api/attendance/{} failed", id, ex);
            throw ex;
        }
    }

    // 7. Get by date (IST/UTC boundary safe)
    @GetMapping("/date/{date}")
    public List<AttendanceResponse> getAttendanceByDate(@PathVariable String date) {
        log.info("API: GET /api/attendance/date/{} - Entered", date);
        try {
            List<AttendanceResponse> responses = service.getAttendanceByDate(LocalDate.parse(date));
            log.info("API: GET /api/attendance/date/{} - Success, found {} records", date, responses.size());
            return responses;
        } catch (Exception ex) {
            log.error("API: GET /api/attendance/date/{} - Failed: {}", date, ex.getMessage(), ex);
            throw ex;
        }
    }

    // 8. Get weekly timesheet view for employee (current week etc.)
    @GetMapping("/employee/{empId}/week")
    public List<AttendanceResponse> getWeeklyTimesheet(
            @PathVariable String empId,
            @RequestParam String weekStart // e.g. "2025-11-10"
    ) {
        log.info("API: GET /api/attendance/employee/{}/week?weekStart={}", empId, weekStart);
        try {
            LocalDate monday = LocalDate.parse(weekStart);
            List<AttendanceResponse> result = service.getWeeklyTimesheet(empId, monday);
            log.info("API: /employee/{}/week => {} days", empId, result.size());
            return result;
        } catch (Exception ex) {
            log.error("API: GET /api/attendance/employee/{}/week failed, weekStart={}", empId, weekStart, ex);
            throw ex;
        }
    }
}
