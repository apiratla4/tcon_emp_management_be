package com.tcon.empManagement.Controller;

import com.tcon.empManagement.Dto.ClientOnBoardCreateRequest;
import com.tcon.empManagement.Dto.ClientOnBoardResponse;
import com.tcon.empManagement.Dto.ClientOnBoardUpdateRequest;
import com.tcon.empManagement.Service.ClientOnBoardService;
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
@RequestMapping("/api/client-onboard")
@RequiredArgsConstructor
@Slf4j
public class ClientOnBoardController {


    private final ClientOnBoardService service;

    // Create a client onboarding record (201 + Location)
    @PostMapping
    public ResponseEntity<ClientOnBoardResponse> create(@Valid @RequestBody ClientOnBoardCreateRequest req) {
        log.info("POST /api/client-onboard projectId={}", req.getProjectId());
        try {
            ClientOnBoardResponse res = service.create(req);
            return ResponseEntity.created(URI.create("/api/client-onboard/" + res.getId())).body(res);
        } catch (Exception ex) {
            log.error("POST /api/client-onboard failed projectId={}", req.getProjectId(), ex);
            throw ex;
        }
    }

    // Partial update by id (PATCH)
    @PatchMapping("/{id}")
    public ClientOnBoardResponse update(@PathVariable String id,
                                        @Valid @RequestBody ClientOnBoardUpdateRequest req) {
        log.info("PATCH /api/client-onboard/{}", id);
        try {
            return service.updateById(id, req);
        } catch (Exception ex) {
            log.error("PATCH /api/client-onboard/{} failed", id, ex);
            throw ex;
        }
    }

    // Get single by id
    @GetMapping("/{id}")
    public ClientOnBoardResponse get(@PathVariable String id) {
        log.info("GET /api/client-onboard/{}", id);
        try {
            return service.getById(id);
        } catch (Exception ex) {
            log.error("GET /api/client-onboard/{} failed", id, ex);
            throw ex;
        }
    }

    // Get single by projectId (business key)
    @GetMapping("/by-project/{projectId}")
    public ClientOnBoardResponse getByProject(@PathVariable String projectId) {
        log.info("GET /api/client-onboard/by-project/{}", projectId);
        try {
            return service.getByProjectId(projectId);
        } catch (Exception ex) {
            log.error("GET /api/client-onboard/by-project/{} failed", projectId, ex);
            throw ex;
        }
    }

    // List paged
    @GetMapping
    public Page<ClientOnBoardResponse> list(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "20") int size) {
        log.info("GET /api/client-onboard?page={}&size={}", page, size);
        try {
            return service.list(PageRequest.of(page, size));
        } catch (Exception ex) {
            log.error("GET /api/client-onboard failed page={} size={}", page, size, ex);
            throw ex;
        }
    }

    // List all by projectId
    @GetMapping("/list/by-project/{projectId}")
    public List<ClientOnBoardResponse> listByProject(@PathVariable String projectId) {
        log.info("GET /api/client-onboard/list/by-project/{}", projectId);
        try {
            return service.listByProjectId(projectId);
        } catch (Exception ex) {
            log.error("GET /api/client-onboard/list/by-project/{} failed", projectId, ex);
            throw ex;
        }
    }

    // Delete by id (204 No Content)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        log.info("DELETE /api/client-onboard/{}", id);
        try {
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception ex) {
            log.error("DELETE /api/client-onboard/{} failed", id, ex);
            throw ex;
        }
    }

    // Bulk delete by projectId (200 + deleted count)
    @DeleteMapping("/by-project/{projectId}")
    public ResponseEntity<Map<String, Object>> deleteByProject(@PathVariable String projectId) {
        log.info("DELETE /api/client-onboard/by-project/{}", projectId);
        try {
            long count = service.deleteAllByProjectId(projectId);
            return ResponseEntity.ok(Map.of("deleted", count));
        } catch (Exception ex) {
            log.error("DELETE /api/client-onboard/by-project/{} failed", projectId, ex);
            throw ex;
        }
    }
}