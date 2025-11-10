package com.tcon.empManagement.Controller;

import com.tcon.empManagement.Dto.StoryTableCreateRequest;
import com.tcon.empManagement.Dto.StoryTableResponse;
import com.tcon.empManagement.Dto.StoryTableUpdateRequest;
import com.tcon.empManagement.Service.StoryTableService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/story-table")
@CrossOrigin(origins = "*")
public class StoryTableController {

    @Autowired
    private StoryTableService storyTableService;

    @PostMapping
    public ResponseEntity<StoryTableResponse> createStoryTable(@Valid @RequestBody StoryTableCreateRequest request) {
        StoryTableResponse response = storyTableService.createStoryTable(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StoryTableResponse> updateStoryTable(@PathVariable String id,
                                                               @RequestBody StoryTableUpdateRequest request) {
        StoryTableResponse response = storyTableService.updateStoryTable(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoryTableResponse> getStoryTableById(@PathVariable String id) {
        StoryTableResponse response = storyTableService.getStoryTableById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<StoryTableResponse>> getAllStoryTables() {
        List<StoryTableResponse> responses = storyTableService.getAllStoryTables();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/assigned/{assignedTo}")
    public ResponseEntity<List<StoryTableResponse>> getStoryTablesByAssignedTo(@PathVariable String assignedTo) {
        List<StoryTableResponse> responses = storyTableService.getStoryTablesByAssignedTo(assignedTo);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<List<StoryTableResponse>> getStoryTablesByDepartment(@PathVariable String department) {
        List<StoryTableResponse> responses = storyTableService.getStoryTablesByDepartment(department);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<StoryTableResponse>> getStoryTablesByPriority(@PathVariable String priority) {
        List<StoryTableResponse> responses = storyTableService.getStoryTablesByPriority(priority);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<StoryTableResponse>> getStoryTablesByStatus(@PathVariable String status) {
        List<StoryTableResponse> responses = storyTableService.getStoryTablesByStatus(status);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/project/{project}")
    public ResponseEntity<List<StoryTableResponse>> getStoryTablesByProject(@PathVariable String project) {
        List<StoryTableResponse> responses = storyTableService.getStoryTablesByProject(project);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/task/{taskName}")
    public ResponseEntity<List<StoryTableResponse>> getStoryTablesByTaskName(@PathVariable String taskName) {
        List<StoryTableResponse> responses = storyTableService.getStoryTablesByTaskName(taskName);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<StoryTableResponse>> getStoryTablesByType(@PathVariable String type) {
        List<StoryTableResponse> responses = storyTableService.getStoryTablesByType(type);
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStoryTable(@PathVariable String id) {
        storyTableService.deleteStoryTable(id);
        return ResponseEntity.noContent().build();
    }
}
