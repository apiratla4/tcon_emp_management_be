package com.tcon.empManagement.Controller;

import com.tcon.empManagement.Entity.EmployeeHistory;
import com.tcon.empManagement.Service.EmployeeHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/employee-history")
@CrossOrigin(origins = "*")
@Slf4j
public class EmployeeHistoryController {

    @Autowired
    private EmployeeHistoryService employeeHistoryService;

    @GetMapping
    public ResponseEntity<List<EmployeeHistory>> getAllHistory() {
        log.info("GET /api/employee-history - getAllHistory called");
        try {
            List<EmployeeHistory> history = employeeHistoryService.getAllHistory();
            return ResponseEntity.ok(history);
        } catch (Exception ex) {
            log.error("GET /api/employee-history failed: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<EmployeeHistory>> getHistoryByEmployeeId(@PathVariable String employeeId) {
        log.info("GET /api/employee-history/employee/{} - getHistoryByEmployeeId called", employeeId);
        try {
            List<EmployeeHistory> history = employeeHistoryService.getHistoryByEmployeeId(employeeId);
            return ResponseEntity.ok(history);
        } catch (Exception ex) {
            log.error("GET /api/employee-history/employee/{} failed: {}", employeeId, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/empid/{empId}")
    public ResponseEntity<List<EmployeeHistory>> getHistoryByEmpId(@PathVariable String empId) {
        log.info("GET /api/employee-history/empid/{} - getHistoryByEmpId called", empId);
        try {
            List<EmployeeHistory> history = employeeHistoryService.getHistoryByEmpId(empId);
            return ResponseEntity.ok(history);
        } catch (Exception ex) {
            log.error("GET /api/employee-history/empid/{} failed: {}", empId, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/operation/{operation}")
    public ResponseEntity<List<EmployeeHistory>> getHistoryByOperation(@PathVariable String operation) {
        log.info("GET /api/employee-history/operation/{} - getHistoryByOperation called", operation);
        try {
            List<EmployeeHistory> history = employeeHistoryService.getHistoryByOperation(operation);
            return ResponseEntity.ok(history);
        } catch (Exception ex) {
            log.error("GET /api/employee-history/operation/{} failed: {}", operation, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<EmployeeHistory>> getHistoryByStatus(@PathVariable String status) {
        log.info("GET /api/employee-history/status/{} - getHistoryByStatus called", status);
        try {
            List<EmployeeHistory> history = employeeHistoryService.getHistoryByStatus(status);
            return ResponseEntity.ok(history);
        } catch (Exception ex) {
            log.error("GET /api/employee-history/status/{} failed: {}", status, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/changed-by/{changedBy}")
    public ResponseEntity<List<EmployeeHistory>> getHistoryByChangedBy(@PathVariable String changedBy) {
        log.info("GET /api/employee-history/changed-by/{} - getHistoryByChangedBy called", changedBy);
        try {
            List<EmployeeHistory> history = employeeHistoryService.getHistoryByChangedBy(changedBy);
            return ResponseEntity.ok(history);
        } catch (Exception ex) {
            log.error("GET /api/employee-history/changed-by/{} failed: {}", changedBy, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<EmployeeHistory>> getHistoryByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant end) {
        log.info("GET /api/employee-history/date-range?start={}&end={} - getHistoryByDateRange called", start, end);
        try {
            List<EmployeeHistory> history = employeeHistoryService.getHistoryByDateRange(start, end);
            return ResponseEntity.ok(history);
        } catch (Exception ex) {
            log.error("GET /api/employee-history/date-range failed: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
