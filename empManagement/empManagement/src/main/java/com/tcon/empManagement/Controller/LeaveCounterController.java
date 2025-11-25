package com.tcon.empManagement.Controller;

import com.tcon.empManagement.Dto.LeaveCounterResponse;
import com.tcon.empManagement.Dto.LeaveCounterUpdateRequest;
import com.tcon.empManagement.Service.LeaveCounterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leave-counter")
@RequiredArgsConstructor
@Slf4j
public class LeaveCounterController {

    private final LeaveCounterService service;

    // Get leave balance for employee
    @GetMapping("/{empId}")
    public ResponseEntity<LeaveCounterResponse> getByEmpId(@PathVariable String empId) {
        return ResponseEntity.ok(service.getByEmpId(empId));
    }

    // Update leave balance directly (admin/HR action)
    @PutMapping("/{empId}")
    public ResponseEntity<LeaveCounterResponse> updateCounters(
            @PathVariable String empId,
            @RequestBody LeaveCounterUpdateRequest req) {
        return ResponseEntity.ok(service.updateLeaveBalance(empId, req));
    }

    // Get all leave balances
    @GetMapping
    public ResponseEntity<List<LeaveCounterResponse>> getAllCounters() {
        return ResponseEntity.ok(service.getAllLeaveBalances());
    }

    // Deduct leave (manual/admin action - most use case handled on approval automatically)
    @PutMapping("/deduct/{empId}")
    public ResponseEntity<Void> deductLeave(
            @PathVariable String empId,
            @RequestParam String leaveType,
            @RequestParam int noOfDays) {
        service.deductLeave(empId, leaveType, noOfDays);
        return ResponseEntity.noContent().build();
    }

    // Add a public holiday
    @PostMapping("/public-holiday")
    public ResponseEntity<Void> addHoliday(@RequestParam String date) {
        service.addPublicHoliday(date);
        return ResponseEntity.noContent().build();
    }

    // Get all public holidays
    @GetMapping("/public-holiday")
    public ResponseEntity<List<String>> getHolidays() {
        return ResponseEntity.ok(service.getPublicHolidays());
    }

    // Optional: annual carry-forward (run at year-end, admin endpoint)
    @PutMapping("/annual-carry-forward")
    public ResponseEntity<Void> carryForwardAnnualLeaves() {
        service.carryForwardAnnualLeaves();
        return ResponseEntity.noContent().build();
    }

    // Optional: initialize for new employee (run only on onboarding)
    @PostMapping("/initialize")
    public ResponseEntity<LeaveCounterResponse> initializeCounter(@RequestParam String empId) {
        return ResponseEntity.ok(service.initializeCounter(empId));
    }
}
