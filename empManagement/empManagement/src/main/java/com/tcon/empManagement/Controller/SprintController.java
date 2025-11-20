package com.tcon.empManagement.Controller;

import com.tcon.empManagement.Dto.SprintCreateRequest;
import com.tcon.empManagement.Dto.SprintResponse;
import com.tcon.empManagement.Entity.Sprint;
import com.tcon.empManagement.Service.SprintService;
import com.tcon.empManagement.Util.SprintMapper;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sprints")
@Slf4j
public class SprintController {

    private final SprintService sprintService;

    @Autowired
    public SprintController(SprintService sprintService) {
        this.sprintService = sprintService;
    }

    @GetMapping("/{project}")
    public ResponseEntity<List<SprintResponse>> getSprintsForProject(@PathVariable String project) {
        log.info("GET /api/sprints/{} - getSprintsForProject called", project);
        try {
            List<Sprint> sprints = sprintService.getSprintsForProject(project);
            List<SprintResponse> responses = sprints.stream()
                    .map(SprintMapper::toResponse)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(responses);
        } catch (Exception ex) {
            log.error("GET /api/sprints/{} failed: {}", project, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{project}/current")
    public ResponseEntity<SprintResponse> getCurrentSprint(@PathVariable String project) {
        log.info("GET /api/sprints/{}/current - getCurrentSprint called", project);
        try {
            Sprint s = sprintService.getCurrentSprintForProject(project);
            if (s == null) {
                log.info("No current sprint found for project: {}", project);
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(SprintMapper.toResponse(s));
        } catch (Exception ex) {
            log.error("GET /api/sprints/{}/current failed: {}", project, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{project}/generate")
    public ResponseEntity<List<SprintResponse>> generateSprintsForProject(
            @PathVariable String project,
            @RequestParam(required = false) Integer year
    ) {
        log.info("POST /api/sprints/{}/generate - generateSprintsForProject called", project);
        try {
            int y = (year != null) ? year : java.time.LocalDate.now().getYear();
            List<Sprint> created = sprintService.generateSprintsForProject(project, y);
            List<SprintResponse> resp = created.stream().map(SprintMapper::toResponse).collect(Collectors.toList());
            return new ResponseEntity<>(resp, HttpStatus.CREATED);
        } catch (Exception ex) {
            log.error("POST /api/sprints/{}/generate failed: {}", project, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<SprintResponse> createSprint(@Valid @RequestBody SprintCreateRequest request) {
        log.info("POST /api/sprints - createSprint called");
        try {
            Sprint entity = SprintMapper.toEntity(request);
            Sprint saved = sprintService.createSprint(entity);
            return new ResponseEntity<>(SprintMapper.toResponse(saved), HttpStatus.CREATED);
        } catch (Exception ex) {
            log.error("POST /api/sprints failed: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{project}/activate/{sprintNumber}")
    public ResponseEntity<SprintResponse> activateSprint(
            @PathVariable String project,
            @PathVariable Integer sprintNumber
    ) {
        log.info("POST /api/sprints/{}/activate/{} - activateSprint called", project, sprintNumber);
        try {
            Sprint activated = sprintService.activateSprint(project, sprintNumber);
            return ResponseEntity.ok(SprintMapper.toResponse(activated));
        } catch (Exception ex) {
            log.error("POST /api/sprints/{}/activate/{} failed: {}", project, sprintNumber, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<SprintResponse> updateSprint(@PathVariable String id, @Valid @RequestBody SprintCreateRequest request) {
        log.info("PUT /api/sprints/{} - updateSprint called", id);
        try {
            List<Sprint> found = sprintService.getSprintsForProject(request.getProject());
            Sprint existing = found.stream().filter(s -> id.equals(s.getId())).findFirst().orElse(null);
            if (existing == null) {
                log.warn("PUT /api/sprints/{} - Not found", id);
                return ResponseEntity.notFound().build();
            }

            existing.setSprintNumber(request.getSprintNumber());
            existing.setYear(request.getYear());
            existing.setStartDate(request.getStartDate());
            existing.setEndDate(request.getEndDate());
            existing.setActive(request.getActive());

            Sprint saved = sprintService.createSprint(existing);
            return ResponseEntity.ok(SprintMapper.toResponse(saved));
        } catch (Exception ex) {
            log.error("PUT /api/sprints/{} failed: {}", id, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSprint(@PathVariable String id) {
        log.info("DELETE /api/sprints/{} - deleteSprint called", id);
        try {
            if (!sprintService.existsById(id)) {
                log.warn("DELETE /api/sprints/{} - Not found", id);
                return ResponseEntity.notFound().build();
            }
            sprintService.deleteSprintById(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException ex) {
            log.warn("DELETE /api/sprints/{} - Not found (NoSuchElementException)", id);
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            log.error("DELETE /api/sprints/{} failed: {}", id, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{project}/{sprintNumber}")
    public ResponseEntity<SprintResponse> getSprintByProjectAndNumber(
            @PathVariable String project,
            @PathVariable Integer sprintNumber) {
        log.info("GET /api/sprints/{}/{} - getSprintByProjectAndNumber called", project, sprintNumber);
        try {
            Sprint sprint = sprintService.getSprintByProjectAndNumber(project, sprintNumber);
            if (sprint == null) {
                log.warn("GET /api/sprints/{}/{} - Not found", project, sprintNumber);
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(SprintMapper.toResponse(sprint));
        } catch (Exception ex) {
            log.error("GET /api/sprints/{}/{} failed: {}", project, sprintNumber, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
