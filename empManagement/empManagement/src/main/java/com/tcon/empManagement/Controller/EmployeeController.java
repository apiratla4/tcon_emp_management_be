package com.tcon.empManagement.Controller;

import com.tcon.empManagement.Dto.EmployeeCreateRequest;
import com.tcon.empManagement.Dto.EmployeeResponse;
import com.tcon.empManagement.Dto.EmployeeUpdateRequest;
import com.tcon.empManagement.Service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = "*")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // ============ CREATE ============

    @PostMapping
    public ResponseEntity<EmployeeResponse> createEmployee(
            @Valid @RequestBody EmployeeCreateRequest request,
            @RequestParam String createdBy) {
        EmployeeResponse response = employeeService.createEmployee(request, createdBy);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // ============ UPDATE ============

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponse> updateEmployee(
            @PathVariable String id,
            @RequestBody EmployeeUpdateRequest request,
            @RequestParam String updatedBy) {
        EmployeeResponse response = employeeService.updateEmployee(id, request, updatedBy);
        return ResponseEntity.ok(response);
    }

    // ============ DELETE ============

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(
            @PathVariable String id,
            @RequestParam String deletedBy) {
        employeeService.deleteEmployee(id, deletedBy);
        return ResponseEntity.noContent().build();
    }

    // ============ GET BY ID ============

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> getEmployeeById(@PathVariable String id) {
        EmployeeResponse response = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(response);
    }

    // ============ GET BY EMP ID ============

    @GetMapping("/empid/{empId}")
    public ResponseEntity<EmployeeResponse> getEmployeeByEmpId(@PathVariable String empId) {
        EmployeeResponse response = employeeService.getEmployeeByEmpId(empId);
        return ResponseEntity.ok(response);
    }

    // ============ GET BY EMAIL ============

    @GetMapping("/email/{email}")
    public ResponseEntity<EmployeeResponse> getEmployeeByEmail(@PathVariable String email) {
        EmployeeResponse response = employeeService.getEmployeeByEmail(email);
        return ResponseEntity.ok(response);
    }

    // ============ GET ALL ============

    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees() {
        List<EmployeeResponse> responses = employeeService.getAllEmployees();
        return ResponseEntity.ok(responses);
    }

    // ============ GET BY STATUS ============

    @GetMapping("/active")
    public ResponseEntity<List<EmployeeResponse>> getActiveEmployees() {
        List<EmployeeResponse> responses = employeeService.getActiveEmployees();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/inactive")
    public ResponseEntity<List<EmployeeResponse>> getInactiveEmployees() {
        List<EmployeeResponse> responses = employeeService.getInactiveEmployees();
        return ResponseEntity.ok(responses);
    }

    // ============ GET BY ROLE ============

    @GetMapping("/role/{empRole}")
    public ResponseEntity<List<EmployeeResponse>> getEmployeesByRole(@PathVariable String empRole) {
        List<EmployeeResponse> responses = employeeService.getEmployeesByRole(empRole);
        return ResponseEntity.ok(responses);
    }

    // ============ ACTIVATE / DEACTIVATE ============

    @PutMapping("/{id}/activate")
    public ResponseEntity<EmployeeResponse> activateEmployee(
            @PathVariable String id,
            @RequestParam String activatedBy) {
        EmployeeResponse response = employeeService.activateEmployee(id, activatedBy);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<EmployeeResponse> deactivateEmployee(
            @PathVariable String id,
            @RequestParam String deactivatedBy) {
        EmployeeResponse response = employeeService.deactivateEmployee(id, deactivatedBy);
        return ResponseEntity.ok(response);
    }
}
