/*
package com.tcon.empManagement.Controller;

import com.tcon.empManagement.Dto.CompanyDocumentRequestDto;
import com.tcon.empManagement.Dto.CompanyDocumentResponseDto;
import com.tcon.empManagement.Service.CompanyDocumentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/company-documents")
public class CompanyDocumentController {

    private static final Logger logger = LoggerFactory.getLogger(CompanyDocumentController.class);

    private final CompanyDocumentService companyDocumentService;

    @PostMapping
    public ResponseEntity<CompanyDocumentResponseDto> createDocument(@RequestBody CompanyDocumentRequestDto requestDto) {
        try {
            CompanyDocumentResponseDto dto = companyDocumentService.createDocument(requestDto);
            logger.info("Company document created successfully: {}", dto.getId());
            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error creating company document: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<CompanyDocumentResponseDto> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @ModelAttribute CompanyDocumentRequestDto requestDto) {
        try {
            CompanyDocumentResponseDto dto = companyDocumentService.uploadCompanyDocument(file, requestDto);
            logger.info("File uploaded for company document: {}", dto.getId());
            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error uploading company document file: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<CompanyDocumentResponseDto>> getAllDocuments() {
        try {
            List<CompanyDocumentResponseDto> docs = companyDocumentService.getAllDocuments();
            return ResponseEntity.ok(docs);
        } catch (Exception e) {
            logger.error("Error getting all company documents: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<List<CompanyDocumentResponseDto>> getDocumentsByOrganizationId(@PathVariable String organizationId) {
        try {
            List<CompanyDocumentResponseDto> docs = companyDocumentService.getDocumentsByOrganizationId(organizationId);
            return ResponseEntity.ok(docs);
        } catch (Exception e) {
            logger.error("Error getting documents by organizationId {}: {}", organizationId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/company/{companyName}")
    public ResponseEntity<List<CompanyDocumentResponseDto>> getDocumentsByCompanyName(@PathVariable String companyName) {
        try {
            List<CompanyDocumentResponseDto> docs = companyDocumentService.getDocumentsByCompanyName(companyName);
            return ResponseEntity.ok(docs);
        } catch (Exception e) {
            logger.error("Error getting documents by companyName {}: {}", companyName, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/download-url/{documentId}")
    public ResponseEntity<String> getDownloadUrl(@PathVariable String documentId) {
        try {
            String url = companyDocumentService.generateDownloadUrlByDocumentId(documentId);
            logger.info("Generated signed download URL for document: {}", documentId);
            return ResponseEntity.ok(url);
        } catch (Exception e) {
            logger.error("Error generating download URL for documentId {}: {}", documentId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{documentId}")
    public ResponseEntity<Void> deleteDocument(@PathVariable String documentId) {
        try {
            companyDocumentService.deleteDocumentById(documentId);
            logger.info("Deleted company document: {}", documentId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error deleting company document {}: {}", documentId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
*/
