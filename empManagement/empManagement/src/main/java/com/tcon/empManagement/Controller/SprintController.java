package com.tcon.empManagement.Controller;

import com.tcon.empManagement.Dto.SprintCreateRequest;
import com.tcon.empManagement.Dto.SprintResponse;
import com.tcon.empManagement.Entity.Sprint;
import com.tcon.empManagement.Service.SprintService;
import com.tcon.empManagement.Util.SprintMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sprints")
public class SprintController {

    private final SprintService sprintService;

    @Autowired
    public SprintController(SprintService sprintService) {
        this.sprintService = sprintService;
    }

    /**
     * Get all sprints for a project ordered by sprint number.
     */
    @GetMapping("/{project}")
    public ResponseEntity<List<SprintResponse>> getSprintsForProject(@PathVariable String project) {
        List<Sprint> sprints = sprintService.getSprintsForProject(project);
        List<SprintResponse> responses = sprints.stream()
                .map(SprintMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    /**
     * Get the active (current) sprint for a project.
     */
    @GetMapping("/{project}/current")
    public ResponseEntity<SprintResponse> getCurrentSprint(@PathVariable String project) {
        Sprint s = sprintService.getCurrentSprintForProject(project);
        if (s == null) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(SprintMapper.toResponse(s));
    }

    /**
     * Auto-generate 52 weekly sprints for the given project and year.
     * Example: POST /api/sprints/PROJECT-A/generate?year=2025
     */
    @PostMapping("/{project}/generate")
    public ResponseEntity<List<SprintResponse>> generateSprintsForProject(
            @PathVariable String project,
            @RequestParam(required = false) Integer year
    ) {
        int y = (year != null) ? year : java.time.LocalDate.now().getYear();
        List<Sprint> created = sprintService.generateSprintsForProject(project, y);
        List<SprintResponse> resp = created.stream().map(SprintMapper::toResponse).collect(Collectors.toList());
        return new ResponseEntity<>(resp, HttpStatus.CREATED);
    }

    /**
     * Create a single sprint (manual).
     */
    @PostMapping
    public ResponseEntity<SprintResponse> createSprint(@Valid @RequestBody SprintCreateRequest request) {
        Sprint entity = SprintMapper.toEntity(request);
        Sprint saved = sprintService.createSprint(entity);
        return new ResponseEntity<>(SprintMapper.toResponse(saved), HttpStatus.CREATED);
    }

    /**
     * Activate a sprint for a project (deactivates previous active sprint)
     */
    @PostMapping("/{project}/activate/{sprintNumber}")
    public ResponseEntity<SprintResponse> activateSprint(
            @PathVariable String project,
            @PathVariable Integer sprintNumber
    ) {
        Sprint activated = sprintService.activateSprint(project, sprintNumber);
        return ResponseEntity.ok(SprintMapper.toResponse(activated));
    }

    /**
     * Update sprint by id.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SprintResponse> updateSprint(@PathVariable String id, @Valid @RequestBody SprintCreateRequest request) {
        // load existing to preserve any fields not provided (simple pattern)
        List<Sprint> found = sprintService.getSprintsForProject(request.getProject());
        Sprint existing = found.stream().filter(s -> id.equals(s.getId())).findFirst().orElse(null);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        // map request -> entity (only updating conservative fields)
        existing.setSprintNumber(request.getSprintNumber());
        existing.setYear(request.getYear());
        existing.setStartDate(request.getStartDate());
        existing.setEndDate(request.getEndDate());
        existing.setActive(request.getActive());

        Sprint saved = sprintService.createSprint(existing); // createSprint handles active flag cleanup
        return ResponseEntity.ok(SprintMapper.toResponse(saved));
    }

    /**
     * Delete sprint by id.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSprint(@PathVariable String id) {
        try {
            if (!sprintService.existsById(id)) {
                return ResponseEntity.notFound().build();
            }

            sprintService.deleteSprintById(id);
            return ResponseEntity.noContent().build();

        } catch (NoSuchElementException ex) {
            return ResponseEntity.notFound().build();

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
