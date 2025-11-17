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

    // Get all attendance (Manager, HR, Owner) - paginated
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

    // Get attendance by employeeId (Employee can view own records)
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

    // Check-in (All employees)
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

    // Check-out (All employees)
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

    // Optional: Get by id
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

    // Optional: Delete by id (admin only)
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

    @GetMapping("/date/{date}")
    public List<AttendanceResponse> getAttendanceByDate(@PathVariable String date) {
        log.info("GET /api/attendance/date/{}", date);
        try {
            return service.getAttendanceByDate(LocalDate.parse(date));
        } catch (Exception ex) {
            log.error("GET /api/attendance/date/{} failed", date, ex);
            throw ex;
        }
    }
}
