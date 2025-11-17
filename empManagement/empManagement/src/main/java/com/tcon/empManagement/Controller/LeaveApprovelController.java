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
        LeaveApprovelResponse res = service.applyLeave(req);
        return ResponseEntity.created(URI.create("/api/leave-approvel/" + res.getId())).body(res);
    }

    // Approve/Reject: PATCH, HR/Owner dashboard
    @PatchMapping("/{id}/status")
    public LeaveApprovelResponse updateStatus(@PathVariable String id, @Valid @RequestBody LeaveApprovelUpdateStatusRequest req) {
        log.info("PATCH /api/leave-approvel/{}/status status={}", id, req.getStatus());
        return service.updateStatus(id, req);
    }

    // List own leaves (employee)
    @GetMapping("/employee/{empId}")
    public List<LeaveApprovelResponse> getByEmpId(@PathVariable String empId) {
        log.info("GET /api/leave-approvel/employee/{}", empId);
        return service.getByEmpId(empId);
    }

    // Get all (paged) for HR/Owner dashboard
    @GetMapping
    public Page<LeaveApprovelResponse> getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        log.info("GET /api/leave-approvel?page={}&size={}", page, size);
        return service.getAll(PageRequest.of(page, size));
    }

    // Get leaves by status
    @GetMapping("/status/{status}")
    public List<LeaveApprovelResponse> getByStatus(@PathVariable String status) {
        log.info("GET /api/leave-approvel/status/{}", status);
        return service.getByStatus(status);
    }

    // Get leaves for a date range
    @GetMapping("/range")
    public List<LeaveApprovelResponse> getByDateRange(@RequestParam LocalDate from, @RequestParam LocalDate to) {
        log.info("GET /api/leave-approvel/range?from={}&to={}", from, to);
        return service.getByDateRange(from, to);
    }

    // Cancel/Delete a leave request
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLeave(@PathVariable String id) {
        log.info("DELETE /api/leave-approvel/{}", id);
        boolean deleted = service.deleteLeave(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // Get a single leave by ID
    @GetMapping("/{id}")
    public LeaveApprovelResponse getById(@PathVariable String id) {
        log.info("GET /api/leave-approvel/{}", id);
        return service.getById(id);
    }

    @GetMapping("/view")
    public List<LeaveApprovelResponse> viewForRole(@RequestParam String role) {
        log.info("GET /api/leave-approvel/view?roles={}", role);
        return service.getLeavesForRole(role);
    }

}
