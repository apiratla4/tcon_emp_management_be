package com.tcon.empManagement.Controller;

import com.tcon.empManagement.Dto.EmployeeDocumentRequestDto;
import com.tcon.empManagement.Dto.EmployeeDocumentResponseDto;
import com.tcon.empManagement.Service.EmployeeDocumentService;
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
@RequestMapping("/api/employee-documents")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EmployeeDocumentController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeDocumentController.class);

    private final EmployeeDocumentService documentService;

    /**
     * Create a new employee document
     * POST /api/employee-documents
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createDocument(@RequestBody EmployeeDocumentRequestDto requestDto) {
        logger.info("REST request to create employee document for empId: {}", requestDto.getEmpId());
        Map<String, Object> response = new HashMap<>();

        try {
            EmployeeDocumentResponseDto createdDocument = documentService.createDocument(requestDto);
            response.put("success", true);
            response.put("message", "Document created successfully");
            response.put("data", createdDocument);
            logger.info("Document created successfully with ID: {}", createdDocument.getId());
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e) {
            logger.error("Error creating document: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", "Failed to create document: " + e.getMessage());
            response.put("data", null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Update an existing employee document
     * PUT /api/employee-documents/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateDocument(
            @PathVariable("id") String documentId,
            @RequestBody EmployeeDocumentRequestDto requestDto) {
        logger.info("REST request to update employee document with ID: {}", documentId);
        Map<String, Object> response = new HashMap<>();

        try {
            EmployeeDocumentResponseDto updatedDocument = documentService.updateDocument(documentId, requestDto);
            response.put("success", true);
            response.put("message", "Document updated successfully");
            response.put("data", updatedDocument);
            logger.info("Document updated successfully with ID: {}", documentId);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (RuntimeException e) {
            logger.error("Error updating document: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            logger.error("Error updating document: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", "Failed to update document: " + e.getMessage());
            response.put("data", null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get a single employee document by ID
     * GET /api/employee-documents/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getDocumentById(@PathVariable("id") String documentId) {
        logger.info("REST request to get employee document with ID: {}", documentId);
        Map<String, Object> response = new HashMap<>();

        try {
            EmployeeDocumentResponseDto document = documentService.getDocumentById(documentId);
            response.put("success", true);
            response.put("message", "Document fetched successfully");
            response.put("data", document);
            logger.info("Document fetched successfully with ID: {}", documentId);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (RuntimeException e) {
            logger.error("Error fetching document: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            logger.error("Error fetching document: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", "Failed to fetch document: " + e.getMessage());
            response.put("data", null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get all employee documents
     * GET /api/employee-documents
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllDocuments() {
        logger.info("REST request to get all employee documents");
        Map<String, Object> response = new HashMap<>();

        try {
            List<EmployeeDocumentResponseDto> documents = documentService.getAllDocuments();
            response.put("success", true);
            response.put("message", "Documents fetched successfully");
            response.put("data", documents);
            response.put("count", documents.size());
            logger.info("Total documents fetched: {}", documents.size());
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            logger.error("Error fetching all documents: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", "Failed to fetch documents: " + e.getMessage());
            response.put("data", null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get all documents for a specific employee
     * GET /api/employee-documents/employee/{empId}
     */
    @GetMapping("/employee/{empId}")
    public ResponseEntity<Map<String, Object>> getDocumentsByEmployeeId(@PathVariable("empId") String empId) {
        logger.info("REST request to get documents for employee ID: {}", empId);
        Map<String, Object> response = new HashMap<>();

        try {
            List<EmployeeDocumentResponseDto> documents = documentService.getDocumentsByEmployeeId(empId);
            response.put("success", true);
            response.put("message", "Documents fetched successfully for employee");
            response.put("data", documents);
            response.put("count", documents.size());
            logger.info("Total documents fetched for employee {}: {}", empId, documents.size());
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            logger.error("Error fetching documents for employee: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", "Failed to fetch documents: " + e.getMessage());
            response.put("data", null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Delete a single employee document
     * DELETE /api/employee-documents/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteDocument(@PathVariable("id") String documentId) {
        logger.info("REST request to delete employee document with ID: {}", documentId);
        Map<String, Object> response = new HashMap<>();

        try {
            documentService.deleteDocument(documentId);
            response.put("success", true);
            response.put("message", "Document deleted successfully");
            response.put("data", null);
            logger.info("Document deleted successfully with ID: {}", documentId);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (RuntimeException e) {
            logger.error("Error deleting document: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            logger.error("Error deleting document: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", "Failed to delete document: " + e.getMessage());
            response.put("data", null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Delete all documents for a specific employee
     * DELETE /api/employee-documents/employee/{empId}
     */
    @DeleteMapping("/employee/{empId}")
    public ResponseEntity<Map<String, Object>> deleteDocumentsByEmployeeId(@PathVariable("empId") String empId) {
        logger.info("REST request to delete all documents for employee ID: {}", empId);
        Map<String, Object> response = new HashMap<>();

        try {
            documentService.deleteDocumentsByEmployeeId(empId);
            response.put("success", true);
            response.put("message", "All documents deleted successfully for employee");
            response.put("data", null);
            logger.info("All documents deleted for employee ID: {}", empId);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            logger.error("Error deleting documents for employee: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", "Failed to delete documents: " + e.getMessage());
            response.put("data", null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
