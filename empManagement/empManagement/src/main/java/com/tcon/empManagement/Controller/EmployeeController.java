package com.tcon.empManagement.Controller;

import com.tcon.empManagement.Dto.EmployeeCreateRequest;
import com.tcon.empManagement.Dto.EmployeeResponse;
import com.tcon.empManagement.Dto.EmployeeUpdateRequest;
import com.tcon.empManagement.Service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
@Slf4j
public class EmployeeController {

    private final EmployeeService service;

    @PostMapping
    public EmployeeResponse create(@Valid @RequestBody EmployeeCreateRequest req) {
        log.info("POST /api/employees empId={} email={}", req.getEmpId(), req.getEmail());  // request context [web:4]
        try {
            EmployeeResponse resp = service.create(req);
            log.info("POST /api/employees success id={}", resp.getId());  // success context [web:4]
            return resp;
        } catch (Exception ex) {
            log.error("POST /api/employees failed empId={} email={}", req.getEmpId(), req.getEmail(), ex);  // enrich context [web:4]
            throw ex; // handled by @RestControllerAdvice [web:12][web:41]
        }
    }

    @PatchMapping("/{id}")
    public EmployeeResponse update(@PathVariable String id, @Valid @RequestBody EmployeeUpdateRequest req) {
        log.info("PATCH /api/employees/{} email={} role={}", id, req.getEmail(), req.getEmpRole());  // request context [web:4]
        try {
            EmployeeResponse resp = service.updateById(id, req);
            // success context [web:4]
            return resp;
        } catch (Exception ex) {
            log.error("PATCH /api/employees/{} failed", id, ex);  // single error log here; advice shapes response [web:12]
            throw ex;
        }
    }

    @GetMapping("/{id}")
    public EmployeeResponse get(@PathVariable String id) {
        log.info("GET /api/employees/{}", id);  // request context [web:4]
        try {
            EmployeeResponse resp = service.getById(id);
            log.info("GET /api/employees/{} success", id);  // success context [web:4]
            return resp;
        } catch (Exception ex) {
            log.error("GET /api/employees/{} failed", id, ex);
            throw ex;
        }
    }

    @GetMapping("/by-empid/{empId}")
    public EmployeeResponse getByEmpId(@PathVariable String empId) {
        log.info("GET /api/employees/by-empid/{}", empId);  // request context [web:4]
        try {
            EmployeeResponse resp = service.getByEmpId(empId);
            log.info("GET /api/employees/by-empid/{} success", empId);  // success context [web:4]
            return resp;
        } catch (Exception ex) {
            log.error("GET /api/employees/by-empid/{} failed", empId, ex);
            throw ex;
        }
    }

    @GetMapping
    public Page<EmployeeResponse> list(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "20") int size) {
        log.info("GET /api/employees?page={}&size={}", page, size);  // request context [web:4]
        try {
            Page<EmployeeResponse> resp = service.list(PageRequest.of(page, size));
            log.info("GET /api/employees success count={}", resp.getNumberOfElements());  // success context [web:4]
            return resp;
        } catch (Exception ex) {
            log.error("GET /api/employees failed page={} size={}", page, size, ex);
            throw ex;
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        log.info("DELETE /api/employees/{}", id);  // request context [web:4]
        try {
            service.deleteById(id);
            log.info("DELETE /api/employees/{} success", id);  // success context [web:4]
        } catch (Exception ex) {
            log.error("DELETE /api/employees/{} failed", id, ex);
            throw ex;
        }
    }
}
