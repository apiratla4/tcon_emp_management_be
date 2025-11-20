package com.tcon.empManagement.Controller;

import com.tcon.empManagement.Dto.StoryTableCreateRequest;
import com.tcon.empManagement.Dto.StoryTableResponse;
import com.tcon.empManagement.Dto.StoryTableUpdateRequest;
import com.tcon.empManagement.Service.StoryTableService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/story-table")
@CrossOrigin(origins = "*")
@Slf4j
public class StoryTableController {

    @Autowired
    private StoryTableService storyTableService;

    @PostMapping
    public ResponseEntity<StoryTableResponse> createStoryTable(@Valid @RequestBody StoryTableCreateRequest request) {
        log.info("POST /api/story-table - createStoryTable called");
        try {
            StoryTableResponse response = storyTableService.createStoryTable(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception ex) {
            log.error("POST /api/story-table failed: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<StoryTableResponse> updateStoryTable(@PathVariable String id,
                                                               @RequestBody StoryTableUpdateRequest request) {
        log.info("PUT /api/story-table/{} - updateStoryTable called", id);
        try {
            StoryTableResponse response = storyTableService.updateStoryTable(id, request);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            log.error("PUT /api/story-table/{} failed: {}", id, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoryTableResponse> getStoryTableById(@PathVariable String id) {
        log.info("GET /api/story-table/{} - getStoryTableById called", id);
        try {
            StoryTableResponse response = storyTableService.getStoryTableById(id);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            log.error("GET /api/story-table/{} failed: {}", id, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<StoryTableResponse>> getAllStoryTables() {
        log.info("GET /api/story-table - getAllStoryTables called");
        try {
            List<StoryTableResponse> responses = storyTableService.getAllStoryTables();
            return ResponseEntity.ok(responses);
        } catch (Exception ex) {
            log.error("GET /api/story-table failed: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/assigned/{assignedTo}")
    public ResponseEntity<List<StoryTableResponse>> getStoryTablesByAssignedTo(@PathVariable String assignedTo) {
        log.info("GET /api/story-table/assigned/{} - getStoryTablesByAssignedTo called", assignedTo);
        try {
            List<StoryTableResponse> responses = storyTableService.getStoryTablesByAssignedTo(assignedTo);
            return ResponseEntity.ok(responses);
        } catch (Exception ex) {
            log.error("GET /api/story-table/assigned/{} failed: {}", assignedTo, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<List<StoryTableResponse>> getStoryTablesByDepartment(@PathVariable String department) {
        log.info("GET /api/story-table/department/{} - getStoryTablesByDepartment called", department);
        try {
            List<StoryTableResponse> responses = storyTableService.getStoryTablesByDepartment(department);
            return ResponseEntity.ok(responses);
        } catch (Exception ex) {
            log.error("GET /api/story-table/department/{} failed: {}", department, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<StoryTableResponse>> getStoryTablesByPriority(@PathVariable String priority) {
        log.info("GET /api/story-table/priority/{} - getStoryTablesByPriority called", priority);
        try {
            List<StoryTableResponse> responses = storyTableService.getStoryTablesByPriority(priority);
            return ResponseEntity.ok(responses);
        } catch (Exception ex) {
            log.error("GET /api/story-table/priority/{} failed: {}", priority, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<StoryTableResponse>> getStoryTablesByStatus(@PathVariable String status) {
        log.info("GET /api/story-table/status/{} - getStoryTablesByStatus called", status);
        try {
            List<StoryTableResponse> responses = storyTableService.getStoryTablesByStatus(status);
            return ResponseEntity.ok(responses);
        } catch (Exception ex) {
            log.error("GET /api/story-table/status/{} failed: {}", status, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/project/{project}")
    public ResponseEntity<List<StoryTableResponse>> getStoryTablesByProject(@PathVariable String project) {
        log.info("GET /api/story-table/project/{} - getStoryTablesByProject called", project);
        try {
            List<StoryTableResponse> responses = storyTableService.getStoryTablesByProject(project);
            return ResponseEntity.ok(responses);
        } catch (Exception ex) {
            log.error("GET /api/story-table/project/{} failed: {}", project, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/task/{taskName}")
    public ResponseEntity<List<StoryTableResponse>> getStoryTablesByTaskName(@PathVariable String taskName) {
        log.info("GET /api/story-table/task/{} - getStoryTablesByTaskName called", taskName);
        try {
            List<StoryTableResponse> responses = storyTableService.getStoryTablesByTaskName(taskName);
            return ResponseEntity.ok(responses);
        } catch (Exception ex) {
            log.error("GET /api/story-table/task/{} failed: {}", taskName, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<StoryTableResponse>> getStoryTablesByType(@PathVariable String type) {
        log.info("GET /api/story-table/type/{} - getStoryTablesByType called", type);
        try {
            List<StoryTableResponse> responses = storyTableService.getStoryTablesByType(type);
            return ResponseEntity.ok(responses);
        } catch (Exception ex) {
            log.error("GET /api/story-table/type/{} failed: {}", type, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStoryTable(@PathVariable String id) {
        log.info("DELETE /api/story-table/{} - deleteStoryTable called", id);
        try {
            storyTableService.deleteStoryTable(id);
            return ResponseEntity.noContent().build();
        } catch (Exception ex) {
            log.error("DELETE /api/story-table/{} failed: {}", id, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
