package com.tcon.empManagement.Controller;

import com.tcon.empManagement.Entity.EmployeeHistory;
import com.tcon.empManagement.Service.EmployeeHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/employee-history")
@CrossOrigin(origins = "*")
public class EmployeeHistoryController {

    @Autowired
    private EmployeeHistoryService employeeHistoryService;

    @GetMapping
    public ResponseEntity<List<EmployeeHistory>> getAllHistory() {
        List<EmployeeHistory> history = employeeHistoryService.getAllHistory();
        return ResponseEntity.ok(history);
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<EmployeeHistory>> getHistoryByEmployeeId(@PathVariable String employeeId) {
        List<EmployeeHistory> history = employeeHistoryService.getHistoryByEmployeeId(employeeId);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/empid/{empId}")
    public ResponseEntity<List<EmployeeHistory>> getHistoryByEmpId(@PathVariable String empId) {
        List<EmployeeHistory> history = employeeHistoryService.getHistoryByEmpId(empId);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/operation/{operation}")
    public ResponseEntity<List<EmployeeHistory>> getHistoryByOperation(@PathVariable String operation) {
        List<EmployeeHistory> history = employeeHistoryService.getHistoryByOperation(operation);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<EmployeeHistory>> getHistoryByStatus(@PathVariable String status) {
        List<EmployeeHistory> history = employeeHistoryService.getHistoryByStatus(status);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/changed-by/{changedBy}")
    public ResponseEntity<List<EmployeeHistory>> getHistoryByChangedBy(@PathVariable String changedBy) {
        List<EmployeeHistory> history = employeeHistoryService.getHistoryByChangedBy(changedBy);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<EmployeeHistory>> getHistoryByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant end) {
        List<EmployeeHistory> history = employeeHistoryService.getHistoryByDateRange(start, end);
        return ResponseEntity.ok(history);
    }
}
