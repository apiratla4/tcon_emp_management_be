package com.tcon.empManagement.Controller;

import com.tcon.empManagement.Dto.EmployeeTaskHistoryCreateRequest;
import com.tcon.empManagement.Dto.EmployeeTaskHistoryResponse;
import com.tcon.empManagement.Dto.EmployeeTaskHistoryUpdateRequest;
import com.tcon.empManagement.Service.EmployeeTaskHistoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/task-history")
@RequiredArgsConstructor
@Slf4j
public class EmployeeTaskHistoryController {

    private final EmployeeTaskHistoryService service;

    @PostMapping
    public ResponseEntity<EmployeeTaskHistoryResponse> create(@Valid @RequestBody EmployeeTaskHistoryCreateRequest req) {
        log.info("POST /api/task-history empId={} empName={}", req.getEmpId(), req.getEmpName());
        try {
            EmployeeTaskHistoryResponse res = service.create(req);
            return ResponseEntity.created(URI.create("/api/task-history/" + res.getId())).body(res);
        } catch (Exception ex) {
            log.error("POST /api/task-history failed empId={}", req.getEmpId(), ex);
            throw ex;
        }
    }

    @PatchMapping("/{id}")
    public EmployeeTaskHistoryResponse update(@PathVariable String id,
                                              @Valid @RequestBody EmployeeTaskHistoryUpdateRequest req) {
        log.info("PATCH /api/task-history/{}", id);
        try {
            return service.updateById(id, req);
        } catch (Exception ex) {
            log.error("PATCH /api/task-history/{} failed", id, ex);
            throw ex;
        }
    }

    @GetMapping("/{id}")
    public EmployeeTaskHistoryResponse get(@PathVariable String id) {
        log.info("GET /api/task-history/{}", id);
        try {
            return service.getById(id);
        } catch (Exception ex) {
            log.error("GET /api/task-history/{} failed", id, ex);
            throw ex;
        }
    }

    @GetMapping
    public Page<EmployeeTaskHistoryResponse> list(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "20") int size) {
        log.info("GET /api/task-history?page={}&size={}", page, size);
        try {
            return service.list(PageRequest.of(page, size));
        } catch (Exception ex) {
            log.error("GET /api/task-history failed page={} size={}", page, size, ex);
            throw ex;
        }
    }

    @GetMapping("/by-emp/{empId}")
    public List<EmployeeTaskHistoryResponse> byEmp(@PathVariable String empId) {
        log.info("GET /api/task-history/by-emp/{}", empId);
        try {
            return service.listByEmpId(empId);
        } catch (Exception ex) {
            log.error("GET /api/task-history/by-emp/{} failed", empId, ex);
            throw ex;
        }
    }

    @GetMapping("/by-status/{status}")
    public List<EmployeeTaskHistoryResponse> byStatus(@PathVariable String status) {
        log.info("GET /api/task-history/by-status/{}", status);
        try {
            return service.listByStatus(status);
        } catch (Exception ex) {
            log.error("GET /api/task-history/by-status/{} failed", status, ex);
            throw ex;
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        log.info("DELETE /api/task-history/{}", id);
        try {
            service.deleteById(id);
        } catch (Exception ex) {
            log.error("DELETE /api/task-history/{} failed", id, ex);
            throw ex;
        }
    }

    @DeleteMapping("/by-emp/{empId}")
    public ResponseEntity<Map<String, Object>> deleteAllByEmpWithCount(@PathVariable String empId) {
        log.info("DELETE /api/task-history/by-emp/{}", empId);
        try {
            long deleted = service.deleteAllByEmpId(empId);
            log.info("Deleted {} records for empId={}", deleted, empId);
            return ResponseEntity.ok(Map.of("deleted", deleted));
        } catch (Exception ex) {
            log.error("DELETE /api/task-history/by-emp/{} failed", empId, ex);
            throw ex;
        }
    }

    // Update guarded by both id and empId
    @PatchMapping("/{id}/emp/{empId}")
    public EmployeeTaskHistoryResponse updateByIdAndEmp(@PathVariable String id,
                                                        @PathVariable String empId,
                                                        @Valid @RequestBody EmployeeTaskHistoryUpdateRequest req) {
        log.info("PATCH /api/task-history/{}/emp/{}", id, empId);
        try {
            return service.updateByIdAndEmpId(id, empId, req);
        } catch (Exception ex) {
            log.error("PATCH /api/task-history/{}/emp/{} failed", id, empId, ex);
            throw ex;
        }
    }

    // Get using only empId (latest or list based on your need)
    @GetMapping("/by-emp/{empId}/latest")
    public EmployeeTaskHistoryResponse getLatestByEmpId(@PathVariable String empId) {
        log.info("GET /api/task-history/by-emp/{}/latest", empId);
        try {
            return service.getLatestByEmpId(empId);
        } catch (Exception ex) {
            log.error("GET /api/task-history/by-emp/{}/latest failed", empId, ex);
            throw ex;
        }
    }


}
