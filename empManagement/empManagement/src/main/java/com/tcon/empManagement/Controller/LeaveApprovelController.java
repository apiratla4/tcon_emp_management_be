package com.tcon.empManagement.Controller;

import com.tcon.empManagement.Dto.LeaveApprovelCreateRequest;
import com.tcon.empManagement.Dto.LeaveApprovelResponse;
import com.tcon.empManagement.Dto.LeaveApprovelUpdateStatusRequest;
import com.tcon.empManagement.Service.LeaveApprovelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/leave-approvel")
@RequiredArgsConstructor
@Slf4j
public class LeaveApprovelController {

    private final LeaveApprovelService service;

    // Apply for leave
    @PostMapping("/apply")
    public ResponseEntity<LeaveApprovelResponse> applyLeave(@Valid @RequestBody LeaveApprovelCreateRequest req) {
        log.info("POST /api/leave-approvel/apply empId={}", req.getEmpId());
        try {
            LeaveApprovelResponse res = service.applyLeave(req);
            return ResponseEntity.created(URI.create("/api/leave-approvel/" + res.getId())).body(res);
        } catch (Exception ex) {
            log.error("POST /api/leave-approvel/apply failed: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Edit leave request - before approval
    @PutMapping("/edit/{id}")
    public ResponseEntity<LeaveApprovelResponse> editLeave(
            @PathVariable String id,
            @Valid @RequestBody LeaveApprovelCreateRequest req) {
        log.info("PUT /api/leave-approvel/edit/{} empId={}", id, req.getEmpId());
        try {
            LeaveApprovelResponse res = service.editLeave(id, req);
            return ResponseEntity.ok(res);
        } catch (Exception ex) {
            log.error("PUT /api/leave-approvel/edit/{} failed: {}", id, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Approve/Reject: PATCH, HR/Owner dashboard
    @PatchMapping("/{id}/status")
    public ResponseEntity<LeaveApprovelResponse> updateStatus(@PathVariable String id, @Valid @RequestBody LeaveApprovelUpdateStatusRequest req) {
        log.info("PATCH /api/leave-approvel/{}/status status={}", id, req.getStatus());
        try {
            LeaveApprovelResponse res = service.updateStatus(id, req);
            return ResponseEntity.ok(res);
        } catch (Exception ex) {
            log.error("PATCH /api/leave-approvel/{}/status failed: {}", id, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // List own leaves (employee)
    @GetMapping("/employee/{empId}")
    public ResponseEntity<List<LeaveApprovelResponse>> getByEmpId(@PathVariable String empId) {
        log.info("GET /api/leave-approvel/employee/{}", empId);
        try {
            List<LeaveApprovelResponse> res = service.getByEmpId(empId);
            return ResponseEntity.ok(res);
        } catch (Exception ex) {
            log.error("GET /api/leave-approvel/employee/{} failed: {}", empId, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get all (paged) for HR/Owner dashboard
    @GetMapping
    public ResponseEntity<Page<LeaveApprovelResponse>> getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        log.info("GET /api/leave-approvel?page={}&size={}", page, size);
        try {
            Page<LeaveApprovelResponse> res = service.getAll(PageRequest.of(page, size));
            return ResponseEntity.ok(res);
        } catch (Exception ex) {
            log.error("GET /api/leave-approvel failed: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get leaves by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<LeaveApprovelResponse>> getByStatus(@PathVariable String status) {
        log.info("GET /api/leave-approvel/status/{}", status);
        try {
            List<LeaveApprovelResponse> res = service.getByStatus(status);
            return ResponseEntity.ok(res);
        } catch (Exception ex) {
            log.error("GET /api/leave-approvel/status/{} failed: {}", status, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get leaves for a date range
    @GetMapping("/range")
    public ResponseEntity<List<LeaveApprovelResponse>> getByDateRange(@RequestParam LocalDate from, @RequestParam LocalDate to) {
        log.info("GET /api/leave-approvel/range?from={}&to={}", from, to);
        try {
            List<LeaveApprovelResponse> res = service.getByDateRange(from, to);
            return ResponseEntity.ok(res);
        } catch (Exception ex) {
            log.error("GET /api/leave-approvel/range failed: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Cancel/Delete a leave request
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLeave(@PathVariable String id) {
        log.info("DELETE /api/leave-approvel/{}", id);
        try {
            boolean deleted = service.deleteLeave(id);
            return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
        } catch (Exception ex) {
            log.error("DELETE /api/leave-approvel/{} failed: {}", id, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get a single leave by ID
    @GetMapping("/{id}")
    public ResponseEntity<LeaveApprovelResponse> getById(@PathVariable String id) {
        log.info("GET /api/leave-approvel/{}", id);
        try {
            LeaveApprovelResponse res = service.getById(id);
            return ResponseEntity.ok(res);
        } catch (Exception ex) {
            log.error("GET /api/leave-approvel/{} failed: {}", id, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/view")
    public ResponseEntity<List<LeaveApprovelResponse>> viewForRole(@RequestParam String role) {
        log.info("GET /api/leave-approvel/view?roles={}", role);
        try {
            List<LeaveApprovelResponse> res = service.getLeavesForRole(role);
            return ResponseEntity.ok(res);
        } catch (Exception ex) {
            log.error("GET /api/leave-approvel/view?roles={} failed: {}", role, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
