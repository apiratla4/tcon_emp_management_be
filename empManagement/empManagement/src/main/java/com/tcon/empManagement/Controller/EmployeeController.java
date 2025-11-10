package com.tcon.empManagement.Controller;

import com.tcon.empManagement.Dto.*;
import com.tcon.empManagement.Service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
@Slf4j
public class EmployeeController {

    private final EmployeeService service;

    // 1. Register/Create employee
    @PostMapping
    public ResponseEntity<EmployeeResponse> create(@Valid @RequestBody EmployeeCreateRequest req) {
        log.info("POST /api/employees empId={} email={}", req.getEmpId(), req.getEmail());
        EmployeeResponse resp = service.create(req);
        log.info("POST /api/employees success id={}", resp.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    // 2. Employee login by empId/password
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {
        log.info("POST /api/employees/login empId={}", req.getEmpId());
        LoginResponse resp = service.loginByEmpIdAndPassword(req);
        log.info("Login successful empId={}", req.getEmpId());
        return ResponseEntity.ok(resp);
    }

    // 3. Update employee details
    @PatchMapping("/{id}")
    public ResponseEntity<EmployeeResponse> update(@PathVariable String id,
                                                   @Valid @RequestBody EmployeeUpdateRequest req) {
        log.info("PATCH /api/employees/{} email={} role={}", id, req.getEmail(), req.getEmpRole());
        EmployeeResponse resp = service.updateById(id, req);
        log.info("PATCH /api/employees/{} success", id);
        return ResponseEntity.ok(resp);
    }

    // 4. Get employee by MongoDB id
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> get(@PathVariable String id) {
        log.info("GET /api/employees/{}", id);
        EmployeeResponse resp = service.getById(id);
        log.info("GET /api/employees/{} success", id);
        return ResponseEntity.ok(resp);
    }

    // 5. Get employee by business empId
    @GetMapping("/by-empid/{empId}")
    public ResponseEntity<EmployeeResponse> getByEmpId(@PathVariable String empId) {
        log.info("GET /api/employees/by-empid/{}", empId);
        EmployeeResponse resp = service.getByEmpId(empId);
        log.info("GET /api/employees/by-empid/{} success", empId);
        return ResponseEntity.ok(resp);
    }

    // 6. List all employees (with pagination)
    @GetMapping
    public ResponseEntity<Page<EmployeeResponse>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("GET /api/employees?page={}&size={}", page, size);
        Page<EmployeeResponse> resp = service.list(PageRequest.of(page, size));
        log.info("GET /api/employees success count={}", resp.getNumberOfElements());
        return ResponseEntity.ok(resp);
    }

    // 7. Delete employee
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        log.info("DELETE /api/employees/{}", id);
        service.deleteById(id);
        log.info("DELETE /api/employees/{} success", id);
        return ResponseEntity.noContent().build();
    }
}
