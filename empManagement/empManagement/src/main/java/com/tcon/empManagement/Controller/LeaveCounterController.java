package com.tcon.empManagement.Controller;

import com.tcon.empManagement.Dto.LeaveCounterResponse;
import com.tcon.empManagement.Dto.LeaveCounterUpdateRequest;
import com.tcon.empManagement.Entity.Employee;
import com.tcon.empManagement.Repository.EmployeeRepository;
import com.tcon.empManagement.Repository.LeaveCounterRepository;
import com.tcon.empManagement.Service.LeaveCounterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/leave-counter")
@RequiredArgsConstructor
@Slf4j
public class LeaveCounterController {

    private final LeaveCounterService service;
    private final EmployeeRepository employeeRepository;  // ✅ Add this
    private final LeaveCounterRepository leaveCounterRepository;  // ✅ Add this

    @GetMapping("/{empId}")
    public ResponseEntity<LeaveCounterResponse> getByEmpId(@PathVariable String empId) {
        try {
            log.debug("Fetching leave counter for empId: {}", empId);
            LeaveCounterResponse response = service.getByEmpId(empId);
            log.debug("Leave counter fetched successfully for empId: {}", empId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching leave counter for empId: {}. Error: {}", empId, e.getMessage(), e);
            throw e;
        }
    }

    @PutMapping("/{empId}")
    public ResponseEntity<LeaveCounterResponse> updateCounters(
            @PathVariable String empId,
            @RequestBody LeaveCounterUpdateRequest req) {
        try {
            log.info("Updating leave counter for empId: {}", empId);
            LeaveCounterResponse response = service.updateLeaveBalance(empId, req);
            log.info("Leave counter updated successfully for empId: {}", empId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error updating leave counter for empId: {}. Error: {}", empId, e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping
    public ResponseEntity<List<LeaveCounterResponse>> getAllCounters() {
        try {
            log.debug("Fetching all leave counters");
            List<LeaveCounterResponse> responses = service.getAllLeaveBalances();
            log.debug("Fetched {} leave counters successfully", responses.size());
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            log.error("Error fetching all leave counters. Error: {}", e.getMessage(), e);
            throw e;
        }
    }

    @PutMapping("/deduct/{empId}")
    public ResponseEntity<Void> deductLeave(
            @PathVariable String empId,
            @RequestParam String leaveType,
            @RequestParam int noOfDays) {
        try {
            log.info("Deducting {} days of {} leave for empId: {}", noOfDays, leaveType, empId);
            service.deductLeave(empId, leaveType, noOfDays);
            log.info("Leave deducted successfully: {} days of {} for empId: {}", noOfDays, leaveType, empId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deducting {} days of {} leave for empId: {}. Error: {}", noOfDays, leaveType, empId, e.getMessage(), e);
            throw e;
        }
    }

    @PutMapping("/restore/{empId}")
    public ResponseEntity<Void> restoreLeave(
            @PathVariable String empId,
            @RequestParam String leaveType,
            @RequestParam int noOfDays) {
        try {
            log.info("Restoring {} days of {} leave for empId: {}", noOfDays, leaveType, empId);
            service.restoreLeave(empId, leaveType, noOfDays);
            log.info("Leave restored successfully: {} days of {} for empId: {}", noOfDays, leaveType, empId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error restoring {} days of {} leave for empId: {}. Error: {}", noOfDays, leaveType, empId, e.getMessage(), e);
            throw e;
        }
    }

    @PostMapping("/public-holiday")
    public ResponseEntity<Void> addHoliday(@RequestParam String date) {
        try {
            log.info("Adding public holiday: {}", date);
            service.addPublicHoliday(date);
            log.info("Public holiday added successfully: {}", date);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error adding public holiday: {}. Error: {}", date, e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/public-holiday")
    public ResponseEntity<List<String>> getHolidays() {
        try {
            log.debug("Fetching all public holidays");
            List<String> holidays = service.getPublicHolidays();
            log.debug("Fetched {} public holidays successfully", holidays.size());
            return ResponseEntity.ok(holidays);
        } catch (Exception e) {
            log.error("Error fetching public holidays. Error: {}", e.getMessage(), e);
            throw e;
        }
    }

    @PostMapping("/initialize")
    public ResponseEntity<LeaveCounterResponse> initializeCounter(@RequestParam String empId) {
        try {
            log.info("Initializing leave counter for new empId: {}", empId);
            LeaveCounterResponse response = service.initializeCounter(empId);
            log.info("Leave counter initialized successfully for empId: {}", empId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error initializing leave counter for empId: {}. Error: {}", empId, e.getMessage(), e);
            throw e;
        }
    }

    // ✅ NEW: Initialize leave counter for a specific employee (path variable)
    @PostMapping("/initialize/{empId}")
    public ResponseEntity<?> initializeCounterForEmployee(@PathVariable String empId) {
        log.info("🚀 Initializing leave counter for empId={}", empId);

        try {
            LeaveCounterResponse response = service.initializeCounter(empId);
            log.info("✅ Successfully initialized leave counter for empId={}", empId);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Leave counter initialized successfully",
                    "data", response
            ));
        } catch (IllegalStateException e) {
            // Already exists
            log.warn("⚠️ Leave counter already exists for empId={}", empId);
            return ResponseEntity.status(409).body(Map.of(
                    "success", false,
                    "message", "Leave counter already exists for employee: " + empId
            ));
        } catch (NoSuchElementException e) {
            // Employee not found
            log.error("❌ Employee not found: {}", empId);
            return ResponseEntity.status(404).body(Map.of(
                    "success", false,
                    "message", "Employee not found: " + empId
            ));
        } catch (Exception e) {
            log.error("❌ Failed to initialize leave counter for empId={}: {}", empId, e.getMessage());
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "message", "Failed to initialize leave counter: " + e.getMessage()
            ));
        }
    }

    // ✅ NEW: Check if leave counter exists for an employee
    @GetMapping("/exists/{empId}")
    public ResponseEntity<Map<String, Object>> checkCounterExists(@PathVariable String empId) {
        log.info("🔍 Checking if leave counter exists for empId={}", empId);

        try {
            LeaveCounterResponse counter = service.getByEmpId(empId);
            return ResponseEntity.ok(Map.of(
                    "exists", true,
                    "empId", empId,
                    "data", counter
            ));
        } catch (NoSuchElementException e) {
            return ResponseEntity.ok(Map.of(
                    "exists", false,
                    "empId", empId,
                    "message", "No leave counter found for this employee"
            ));
        }
    }

    // ✅ NEW: Preview what would happen during bulk initialization (DRY RUN)
    @GetMapping("/initialize-all/preview")
    public ResponseEntity<Map<String, Object>> previewInitialization() {
        log.info("🔍 Preview mode: Checking which employees need initialization");

        List<Employee> allEmployees = employeeRepository.findAll();
        List<String> needsInit = new ArrayList<>();
        List<String> alreadyHas = new ArrayList<>();

        for (Employee emp : allEmployees) {
            if (leaveCounterRepository.findByEmpId(emp.getEmpId()).isPresent()) {
                alreadyHas.add(emp.getEmpId());
            } else {
                needsInit.add(emp.getEmpId());
            }
        }

        return ResponseEntity.ok(Map.of(
                "totalEmployees", allEmployees.size(),
                "willBeInitialized", needsInit.size(),
                "alreadyHaveCounters", alreadyHas.size(),
                "employeesNeedingInit", needsInit,
                "employeesAlreadySetup", alreadyHas
        ));
    }

    // ✅ NEW: Initialize leave counters for ALL employees who don't have one
    @PostMapping("/initialize-all")
    public ResponseEntity<Map<String, Object>> initializeAllEmployeeCounters() {
        log.info("🚀 Starting bulk initialization of leave counters");

        try {
            List<Employee> allEmployees = employeeRepository.findAll();
            int totalEmployees = allEmployees.size();
            int initialized = 0;
            int skipped = 0;
            List<String> errors = new ArrayList<>();

            for (Employee emp : allEmployees) {
                try {
                    service.initializeCounter(emp.getEmpId());
                    initialized++;
                    log.info("✅ Initialized leave counter for empId={}", emp.getEmpId());
                } catch (IllegalStateException e) {
                    // Already exists - skip
                    skipped++;
                    log.debug("⏭️ Skipped empId={}: {}", emp.getEmpId(), e.getMessage());
                } catch (Exception e) {
                    errors.add("Failed for " + emp.getEmpId() + ": " + e.getMessage());
                    log.error("❌ Failed to initialize for empId={}: {}", emp.getEmpId(), e.getMessage());
                }
            }

            Map<String, Object> result = new HashMap<>();
            result.put("totalEmployees", totalEmployees);
            result.put("initialized", initialized);
            result.put("skipped", skipped);
            result.put("errors", errors);
            result.put("success", errors.isEmpty());

            log.info("✅ Bulk initialization complete: total={}, initialized={}, skipped={}, errors={}",
                    totalEmployees, initialized, skipped, errors.size());

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("❌ Error during bulk initialization: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "message", "Bulk initialization failed: " + e.getMessage()
            ));
        }
    }

    @PutMapping("/annual-carry-forward")
    public ResponseEntity<Void> carryForwardAnnualLeaves() {
        try {
            log.info("Executing annual leave carry-forward");
            service.carryForwardAnnualLeaves();
            log.info("Annual leave carry-forward completed successfully");
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error during annual leave carry-forward. Error: {}", e.getMessage(), e);
            throw e;
        }
    }

    @PutMapping("/annual-accrual")
    public ResponseEntity<Void> accrueAnnualLeaveMonthly() {
        try {
            log.info("Executing monthly annual leave accrual");
            service.accrueAnnualLeaveMonthly();
            log.info("Monthly annual leave accrual completed successfully");
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error during monthly annual leave accrual. Error: {}", e.getMessage(), e);
            throw e;
        }
    }
}
