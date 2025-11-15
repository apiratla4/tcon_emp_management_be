/*
package com.tcon.empManagement.Controller;

import com.tcon.empManagement.Dto.EmployeeDocumentCreateDto;
import com.tcon.empManagement.Dto.EmployeeDocumentResponseDto;
import com.tcon.empManagement.Service.EmployeeDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/employee-documents")
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
        EmployeeDocumentResponseDto dto = employeeDocumentService.uploadEmployeeDocument(file, createDto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDocumentResponseDto>> getAllDocuments() {
        List<EmployeeDocumentResponseDto> documents = employeeDocumentService.listAllDocuments();
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/by-emp/{empId}")
    public ResponseEntity<List<EmployeeDocumentResponseDto>> getDocumentsByEmpId(@PathVariable String empId) {
        List<EmployeeDocumentResponseDto> documents = employeeDocumentService.findDocumentsByEmpId(empId);
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/download-url/{fileName}")
    public ResponseEntity<String> getDownloadUrl(@PathVariable String fileName) {
        String url = employeeDocumentService.generateDownloadUrl(fileName);
        return ResponseEntity.ok(url);
    }

    @DeleteMapping("/{documentId}")
    public ResponseEntity<Void> deleteDocument(
            @PathVariable String documentId,
            @RequestParam("updatedBy") String updatedBy) {
        employeeDocumentService.deleteDocument(documentId, updatedBy);
        return ResponseEntity.noContent().build();
    }
}
*/
