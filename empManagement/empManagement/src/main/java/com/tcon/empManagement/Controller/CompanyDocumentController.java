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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/company-documents")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CompanyDocumentController {

    private static final Logger logger = LoggerFactory.getLogger(CompanyDocumentController.class);

    private final CompanyDocumentService documentService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> createDocument(@RequestBody CompanyDocumentRequestDto requestDto) {
        logger.info("REST request to create company document for orgId: {}", requestDto.getOrganizationId());
        Map<String, Object> response = new HashMap<>();
        try {
            CompanyDocumentResponseDto created = documentService.createDocument(requestDto);
            response.put("success", true);
            response.put("message", "Company document created successfully");
            response.put("data", created);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error creating: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateDocument(@PathVariable String id, @RequestBody CompanyDocumentRequestDto requestDto) {
        logger.info("REST request to update company document ID: {}", id);
        Map<String, Object> response = new HashMap<>();
        try {
            CompanyDocumentResponseDto updated = documentService.updateDocument(id, requestDto);
            response.put("success", true);
            response.put("message", "Company document updated successfully");
            response.put("data", updated);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error updating: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getDocumentById(@PathVariable String id) {
        logger.info("REST request to get company document ID: {}", id);
        Map<String, Object> response = new HashMap<>();
        try {
            CompanyDocumentResponseDto doc = documentService.getDocumentById(id);
            response.put("success", true);
            response.put("message", "Company document fetched");
            response.put("data", doc);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error getting: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllDocuments() {
        logger.info("REST request to get all company documents");
        Map<String, Object> response = new HashMap<>();
        try {
            List<CompanyDocumentResponseDto> docs = documentService.getAllDocuments();
            response.put("success", true);
            response.put("message", "Company documents fetched");
            response.put("data", docs);
            response.put("count", docs.size());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error getting all: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteDocumentById(@PathVariable String id) {
        logger.info("REST request to delete company document ID: {}", id);
        Map<String, Object> response = new HashMap<>();
        try {
            documentService.deleteDocumentById(id);
            response.put("success", true);
            response.put("message", "Company document deleted");
            response.put("data", null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error deleting: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/organization/{orgId}")
    public ResponseEntity<Map<String, Object>> getDocumentsByOrganizationId(@PathVariable String orgId) {
        logger.info("REST request to get company documents for orgId: {}", orgId);
        Map<String, Object> response = new HashMap<>();
        try {
            List<CompanyDocumentResponseDto> docs = documentService.getDocumentsByOrganizationId(orgId);
            response.put("success", true);
            response.put("message", "Company documents fetched");
            response.put("data", docs);
            response.put("count", docs.size());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error getting by orgId: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/organization/{orgId}")
    public ResponseEntity<Map<String, Object>> deleteDocumentsByOrganizationId(@PathVariable String orgId) {
        logger.info("REST request to delete company documents for orgId: {}", orgId);
        Map<String, Object> response = new HashMap<>();
        try {
            documentService.deleteDocumentsByOrganizationId(orgId);
            response.put("success", true);
            response.put("message", "Company documents deleted for orgId");
            response.put("data", null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error deleting by orgId: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/company/{name}")
    public ResponseEntity<Map<String, Object>> getDocumentsByCompanyName(@PathVariable String name) {
        logger.info("REST request to get company documents for companyName: {}", name);
        Map<String, Object> response = new HashMap<>();
        try {
            List<CompanyDocumentResponseDto> docs = documentService.getDocumentsByCompanyName(name);
            response.put("success", true);
            response.put("message", "Company documents fetched");
            response.put("data", docs);
            response.put("count", docs.size());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error getting by companyName: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/company/{name}")
    public ResponseEntity<Map<String, Object>> deleteDocumentsByCompanyName(@PathVariable String name) {
        logger.info("REST request to delete company documents for companyName: {}", name);
        Map<String, Object> response = new HashMap<>();
        try {
            documentService.deleteDocumentsByCompanyName(name);
            response.put("success", true);
            response.put("message", "Company documents deleted for companyName");
            response.put("data", null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error deleting by companyName: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
