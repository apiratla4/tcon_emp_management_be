package com.tcon.empManagement.Controller;

import com.tcon.empManagement.Dto.EmployeeDocumentCreateDto;
import com.tcon.empManagement.Dto.EmployeeDocumentResponseDto;
import com.tcon.empManagement.Service.EmployeeDocumentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/employee-documents")
@Slf4j
public class EmployeeDocumentController {

    private final EmployeeDocumentService employeeDocumentService;

    @Autowired
    public EmployeeDocumentController(EmployeeDocumentService employeeDocumentService) {
        this.employeeDocumentService = employeeDocumentService;
    }

    @PostMapping("/upload")
    public ResponseEntity<EmployeeDocumentResponseDto> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @ModelAttribute EmployeeDocumentCreateDto createDto
    ) {
        log.info("POST /api/employee-documents/upload - uploadDocument called, empId={}", createDto.getEmpId());
        try {
            EmployeeDocumentResponseDto dto = employeeDocumentService.uploadEmployeeDocument(file, createDto);
            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        } catch (Exception ex) {
            log.error("POST /api/employee-documents/upload failed: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDocumentResponseDto>> getAllDocuments() {
        log.info("GET /api/employee-documents - getAllDocuments called");
        try {
            List<EmployeeDocumentResponseDto> documents = employeeDocumentService.listAllDocuments();
            return ResponseEntity.ok(documents);
        } catch (Exception ex) {
            log.error("GET /api/employee-documents failed: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/by-emp/{empId}")
    public ResponseEntity<List<EmployeeDocumentResponseDto>> getDocumentsByEmpId(@PathVariable String empId) {
        log.info("GET /api/employee-documents/by-emp/{} called", empId);
        try {
            List<EmployeeDocumentResponseDto> documents = employeeDocumentService.findDocumentsByEmpId(empId);
            return ResponseEntity.ok(documents);
        } catch (Exception ex) {
            log.error("GET /api/employee-documents/by-emp/{} failed: {}", empId, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/download-url/{documentId}")
    public ResponseEntity<String> getDownloadUrl(@PathVariable String documentId) {
        log.info("GET /api/employee-documents/download-url/{} called", documentId);
        try {
            String url = employeeDocumentService.generateDownloadUrlByDocumentId(documentId);
            return ResponseEntity.ok(url);
        } catch (Exception ex) {
            log.error("GET /api/employee-documents/download-url/{} failed: {}", documentId, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{documentId}")
    public ResponseEntity<Void> deleteDocument(
            @PathVariable String documentId,
            @RequestParam("updatedBy") String updatedBy) {
        log.info("DELETE /api/employee-documents/{} called, updatedBy={}", documentId, updatedBy);
        try {
            employeeDocumentService.deleteDocument(documentId, updatedBy);
            return ResponseEntity.noContent().build();
        } catch (Exception ex) {
            log.error("DELETE /api/employee-documents/{} failed: {}", documentId, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
