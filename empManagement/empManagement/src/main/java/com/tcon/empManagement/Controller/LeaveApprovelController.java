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
    public LeaveApprovelResponse updateStatus(@PathVariable String id,
                                              @Valid @RequestBody LeaveApprovelUpdateStatusRequest req) {
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
    public Page<LeaveApprovelResponse> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("GET /api/leave-approvel?page={}&size={}", page, size);
        return service.getAll(PageRequest.of(page, size));
    }
}
