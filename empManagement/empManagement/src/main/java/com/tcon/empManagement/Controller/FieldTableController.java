package com.tcon.empManagement.Controller;

import com.tcon.empManagement.Dto.FieldTableCreateRequest;
import com.tcon.empManagement.Dto.FieldTableResponse;
import com.tcon.empManagement.Dto.FieldTableUpdateRequest;
import com.tcon.empManagement.Service.FieldTableService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/field-table")
@CrossOrigin(origins = "*")
public class FieldTableController {

    @Autowired
    private FieldTableService fieldTableService;

    @PostMapping
    public ResponseEntity<FieldTableResponse> createFieldTable(@Valid @RequestBody FieldTableCreateRequest request) {
        FieldTableResponse response = fieldTableService.createFieldTable(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FieldTableResponse> updateFieldTable(@PathVariable String id,
                                                               @RequestBody FieldTableUpdateRequest request) {
        FieldTableResponse response = fieldTableService.updateFieldTable(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FieldTableResponse> getFieldTableById(@PathVariable String id) {
        FieldTableResponse response = fieldTableService.getFieldTableById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<FieldTableResponse>> getAllFieldTables() {
        List<FieldTableResponse> responses = fieldTableService.getAllFieldTables();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/task/{taskName}")
    public ResponseEntity<List<FieldTableResponse>> getFieldTablesByTaskName(@PathVariable String taskName) {
        List<FieldTableResponse> responses = fieldTableService.getFieldTablesByTaskName(taskName);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<FieldTableResponse>> getFieldTablesByPriority(@PathVariable String priority) {
        List<FieldTableResponse> responses = fieldTableService.getFieldTablesByPriority(priority);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<FieldTableResponse>> getFieldTablesByType(@PathVariable String type) {
        List<FieldTableResponse> responses = fieldTableService.getFieldTablesByType(type);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/department/{deptName}")
    public ResponseEntity<List<FieldTableResponse>> getFieldTablesByDeptName(@PathVariable String deptName) {
        List<FieldTableResponse> responses = fieldTableService.getFieldTablesByDeptName(deptName);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/department/{deptName}/priority/{priority}")
    public ResponseEntity<List<FieldTableResponse>> getFieldTablesByDeptNameAndPriority(
            @PathVariable String deptName,
            @PathVariable String priority) {
        List<FieldTableResponse> responses = fieldTableService.getFieldTablesByDeptNameAndPriority(deptName, priority);
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFieldTable(@PathVariable String id) {
        fieldTableService.deleteFieldTable(id);
        return ResponseEntity.noContent().build();
    }
}
