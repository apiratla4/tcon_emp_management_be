package com.tcon.empManagement.Controller;

import com.tcon.empManagement.Dto.FieldTableCreateRequest;
import com.tcon.empManagement.Dto.FieldTableResponse;
import com.tcon.empManagement.Dto.FieldTableUpdateRequest;
import com.tcon.empManagement.Service.FieldTableService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/field-table")
@CrossOrigin(origins = "*")
@Slf4j
public class FieldTableController {

    @Autowired
    private FieldTableService fieldTableService;

    @PostMapping
    public ResponseEntity<FieldTableResponse> createFieldTable(@Valid @RequestBody FieldTableCreateRequest request) {
        log.info("POST /api/field-table - createFieldTable called");
        try {
            FieldTableResponse response = fieldTableService.createFieldTable(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception ex) {
            log.error("POST /api/field-table failed: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<FieldTableResponse> updateFieldTable(@PathVariable String id,
                                                               @RequestBody FieldTableUpdateRequest request) {
        log.info("PUT /api/field-table/{} - updateFieldTable called", id);
        try {
            FieldTableResponse response = fieldTableService.updateFieldTable(id, request);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            log.error("PUT /api/field-table/{} failed: {}", id, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<FieldTableResponse> getFieldTableById(@PathVariable String id) {
        log.info("GET /api/field-table/{} - getFieldTableById called", id);
        try {
            FieldTableResponse response = fieldTableService.getFieldTableById(id);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            log.error("GET /api/field-table/{} failed: {}", id, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<FieldTableResponse>> getAllFieldTables() {
        log.info("GET /api/field-table - getAllFieldTables called");
        try {
            List<FieldTableResponse> responses = fieldTableService.getAllFieldTables();
            return ResponseEntity.ok(responses);
        } catch (Exception ex) {
            log.error("GET /api/field-table failed: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/task/{taskName}")
    public ResponseEntity<List<FieldTableResponse>> getFieldTablesByTaskName(@PathVariable String taskName) {
        log.info("GET /api/field-table/task/{} - getFieldTablesByTaskName called", taskName);
        try {
            List<FieldTableResponse> responses = fieldTableService.getFieldTablesByTaskName(taskName);
            return ResponseEntity.ok(responses);
        } catch (Exception ex) {
            log.error("GET /api/field-table/task/{} failed: {}", taskName, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<FieldTableResponse>> getFieldTablesByPriority(@PathVariable String priority) {
        log.info("GET /api/field-table/priority/{} - getFieldTablesByPriority called", priority);
        try {
            List<FieldTableResponse> responses = fieldTableService.getFieldTablesByPriority(priority);
            return ResponseEntity.ok(responses);
        } catch (Exception ex) {
            log.error("GET /api/field-table/priority/{} failed: {}", priority, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<FieldTableResponse>> getFieldTablesByType(@PathVariable String type) {
        log.info("GET /api/field-table/type/{} - getFieldTablesByType called", type);
        try {
            List<FieldTableResponse> responses = fieldTableService.getFieldTablesByType(type);
            return ResponseEntity.ok(responses);
        } catch (Exception ex) {
            log.error("GET /api/field-table/type/{} failed: {}", type, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/department/{deptName}")
    public ResponseEntity<List<FieldTableResponse>> getFieldTablesByDeptName(@PathVariable String deptName) {
        log.info("GET /api/field-table/department/{} - getFieldTablesByDeptName called", deptName);
        try {
            List<FieldTableResponse> responses = fieldTableService.getFieldTablesByDeptName(deptName);
            return ResponseEntity.ok(responses);
        } catch (Exception ex) {
            log.error("GET /api/field-table/department/{} failed: {}", deptName, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/department/{deptName}/priority/{priority}")
    public ResponseEntity<List<FieldTableResponse>> getFieldTablesByDeptNameAndPriority(
            @PathVariable String deptName,
            @PathVariable String priority) {
        log.info("GET /api/field-table/department/{}/priority/{} - getFieldTablesByDeptNameAndPriority called", deptName, priority);
        try {
            List<FieldTableResponse> responses = fieldTableService.getFieldTablesByDeptNameAndPriority(deptName, priority);
            return ResponseEntity.ok(responses);
        } catch (Exception ex) {
            log.error("GET /api/field-table/department/{}/priority/{} failed: {}", deptName, priority, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFieldTable(@PathVariable String id) {
        log.info("DELETE /api/field-table/{} - deleteFieldTable called", id);
        try {
            fieldTableService.deleteFieldTable(id);
            return ResponseEntity.noContent().build();
        } catch (Exception ex) {
            log.error("DELETE /api/field-table/{} failed: {}", id, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
