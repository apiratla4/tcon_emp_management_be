package com.tcon.empManagement.Service;

import com.tcon.empManagement.Dto.EmployeeDocumentRequestDto;
import com.tcon.empManagement.Dto.EmployeeDocumentResponseDto;
import com.tcon.empManagement.Entity.Employee;
import com.tcon.empManagement.Entity.EmployeeDocument;
import com.tcon.empManagement.Repository.EmployeeDocumentRepository;
import com.tcon.empManagement.Repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeDocumentServiceImpl implements EmployeeDocumentService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeDocumentServiceImpl.class);

    private final EmployeeDocumentRepository documentRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional
    public EmployeeDocumentResponseDto createDocument(EmployeeDocumentRequestDto requestDto) {
        logger.info("Creating document for employee ID: {}, file: {}", requestDto.getEmpId(), requestDto.getFileName());

        try {
            // Step 1: Validate employee exists in Employee collection
            if (!validateEmployeeExists(requestDto.getEmpId())) {
                logger.error("Employee not found with ID: {}", requestDto.getEmpId());
                throw new RuntimeException("Employee not found with ID: " + requestDto.getEmpId());
            }

            // Step 2: Check if document already exists
            if (documentRepository.existsByEmpIdAndFileName(requestDto.getEmpId(), requestDto.getFileName())) {
                logger.error("Document already exists for employee: {} with filename: {}",
                        requestDto.getEmpId(), requestDto.getFileName());
                throw new RuntimeException("Document with same filename already exists for this employee");
            }

            // Step 3: Fetch employee name from Employee collection
            String empName = getEmployeeNameFromTable(requestDto.getEmpId());

            // Step 4: Create new document entity
            EmployeeDocument document = new EmployeeDocument();
            document.setEmpId(requestDto.getEmpId());
            document.setEmpName(empName);
            document.setFileName(requestDto.getFileName());
            document.setFileSize(requestDto.getFileSize());
            document.setFileUrl(requestDto.getFileUrl());
            document.setNote(requestDto.getNote());
            document.setCreatedAt(LocalDateTime.now());
            document.setUpdatedAt(LocalDateTime.now());
            document.setUpdatedBy(requestDto.getEmpId());

            // Step 5: Save to database
            EmployeeDocument savedDocument = documentRepository.save(document);
            logger.info("Document created successfully with ID: {} for employee: {}",
                    savedDocument.getId(), requestDto.getEmpId());

            return mapToResponseDto(savedDocument);

        } catch (RuntimeException e) {
            logger.error("Runtime error creating document for employee: {}, Error: {}",
                    requestDto.getEmpId(), e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error creating document for employee: {}, Error: {}",
                    requestDto.getEmpId(), e.getMessage(), e);
            throw new RuntimeException("Failed to create document: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public EmployeeDocumentResponseDto updateDocument(String documentId, EmployeeDocumentRequestDto requestDto) {
        logger.info("Updating document ID: {}", documentId);

        try {
            // Find existing document
            EmployeeDocument existingDocument = documentRepository.findById(documentId)
                    .orElseThrow(() -> {
                        logger.error("Document not found with ID: {}", documentId);
                        return new RuntimeException("Document not found with ID: " + documentId);
                    });

            // Validate updatedBy employee exists
            if (requestDto.getUpdatedBy() != null && !requestDto.getUpdatedBy().isEmpty()) {
                if (!validateEmployeeExists(requestDto.getUpdatedBy())) {
                    logger.error("UpdatedBy employee not found with ID: {}", requestDto.getUpdatedBy());
                    throw new RuntimeException("UpdatedBy employee not found with ID: " + requestDto.getUpdatedBy());
                }
            }

            // Update fields
            if (requestDto.getFileName() != null && !requestDto.getFileName().isEmpty()) {
                existingDocument.setFileName(requestDto.getFileName());
            }
            if (requestDto.getFileSize() != null) {
                existingDocument.setFileSize(requestDto.getFileSize());
            }
            if (requestDto.getFileUrl() != null && !requestDto.getFileUrl().isEmpty()) {
                existingDocument.setFileUrl(requestDto.getFileUrl());
            }
            if (requestDto.getNote() != null) {
                existingDocument.setNote(requestDto.getNote());
            }

            existingDocument.setUpdatedAt(LocalDateTime.now());
            existingDocument.setUpdatedBy(requestDto.getUpdatedBy());

            // Refresh employee name in case it changed
            String updatedEmpName = getEmployeeNameFromTable(existingDocument.getEmpId());
            existingDocument.setEmpName(updatedEmpName);

            // Save updated document
            EmployeeDocument updatedDocument = documentRepository.save(existingDocument);
            logger.info("Document updated successfully with ID: {}", updatedDocument.getId());

            return mapToResponseDto(updatedDocument);

        } catch (RuntimeException e) {
            logger.error("Runtime error updating document ID: {}, Error: {}", documentId, e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error updating document ID: {}, Error: {}", documentId, e.getMessage(), e);
            throw new RuntimeException("Failed to update document: " + e.getMessage(), e);
        }
    }

    @Override
    public EmployeeDocumentResponseDto getDocumentById(String documentId) {
        logger.info("Fetching document with ID: {}", documentId);

        try {
            EmployeeDocument document = documentRepository.findById(documentId)
                    .orElseThrow(() -> {
                        logger.error("Document not found with ID: {}", documentId);
                        return new RuntimeException("Document not found with ID: " + documentId);
                    });

            logger.info("Document fetched successfully with ID: {}", documentId);
            return mapToResponseDto(document);

        } catch (RuntimeException e) {
            logger.error("Runtime error fetching document ID: {}, Error: {}", documentId, e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error fetching document ID: {}, Error: {}", documentId, e.getMessage(), e);
            throw new RuntimeException("Failed to fetch document: " + e.getMessage(), e);
        }
    }

    @Override
    public List<EmployeeDocumentResponseDto> getAllDocuments() {
        logger.info("Fetching all documents");

        try {
            List<EmployeeDocument> documents = documentRepository.findAll();
            logger.info("Total documents fetched: {}", documents.size());

            return documents.stream()
                    .map(this::mapToResponseDto)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            logger.error("Error fetching all documents, Error: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch documents: " + e.getMessage(), e);
        }
    }

    @Override
    public List<EmployeeDocumentResponseDto> getDocumentsByEmployeeId(String empId) {
        logger.info("Fetching documents for employee ID: {}", empId);

        try {
            // Validate employee exists
            if (!validateEmployeeExists(empId)) {
                logger.error("Employee not found with ID: {}", empId);
                throw new RuntimeException("Employee not found with ID: " + empId);
            }

            List<EmployeeDocument> documents = documentRepository.findByEmpId(empId);
            logger.info("Total documents fetched for employee {}: {}", empId, documents.size());

            return documents.stream()
                    .map(this::mapToResponseDto)
                    .collect(Collectors.toList());

        } catch (RuntimeException e) {
            logger.error("Runtime error fetching documents for employee: {}, Error: {}", empId, e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error fetching documents for employee: {}, Error: {}", empId, e.getMessage(), e);
            throw new RuntimeException("Failed to fetch documents for employee: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void deleteDocument(String documentId) {
        logger.info("Deleting document with ID: {}", documentId);

        try {
            if (!documentRepository.existsById(documentId)) {
                logger.error("Document not found with ID: {}", documentId);
                throw new RuntimeException("Document not found with ID: " + documentId);
            }

            documentRepository.deleteById(documentId);
            logger.info("Document deleted successfully with ID: {}", documentId);

        } catch (RuntimeException e) {
            logger.error("Runtime error deleting document ID: {}, Error: {}", documentId, e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error deleting document ID: {}, Error: {}", documentId, e.getMessage(), e);
            throw new RuntimeException("Failed to delete document: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void deleteDocumentsByEmployeeId(String empId) {
        logger.info("Deleting all documents for employee ID: {}", empId);

        try {
            // Validate employee exists
            if (!validateEmployeeExists(empId)) {
                logger.error("Employee not found with ID: {}", empId);
                throw new RuntimeException("Employee not found with ID: " + empId);
            }

            List<EmployeeDocument> documents = documentRepository.findByEmpId(empId);
            if (documents.isEmpty()) {
                logger.warn("No documents found for employee ID: {}", empId);
                return;
            }

            documentRepository.deleteByEmpId(empId);
            logger.info("All documents deleted for employee ID: {}, Total deleted: {}", empId, documents.size());

        } catch (RuntimeException e) {
            logger.error("Runtime error deleting documents for employee: {}, Error: {}", empId, e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error deleting documents for employee: {}, Error: {}", empId, e.getMessage(), e);
            throw new RuntimeException("Failed to delete documents for employee: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean validateEmployeeExists(String empId) {
        logger.debug("Validating employee exists with ID: {}", empId);

        try {
            boolean exists = employeeRepository.existsByEmpId(empId);
            logger.debug("Employee {} exists: {}", empId, exists);
            return exists;

        } catch (Exception e) {
            logger.error("Error validating employee ID: {}, Error: {}", empId, e.getMessage(), e);
            return false;
        }
    }

    /**
     * Helper method to fetch employee name from Employee collection
     * Uses optimized query to fetch only firstName and lastName
     */
    private String getEmployeeNameFromTable(String empId) {
        logger.debug("Fetching employee name for ID: {}", empId);

        try {
            // Use optimized query that fetches only name fields
            Employee employee = employeeRepository.findNameByEmpId(empId)
                    .orElseThrow(() -> {
                        logger.error("Employee not found with ID: {}", empId);
                        return new RuntimeException("Employee not found with ID: " + empId);
                    });

            String fullName = employee.getFullName().trim();
            logger.debug("Employee name fetched: {}", fullName);
            return fullName;

        } catch (RuntimeException e) {
            logger.error("Runtime error fetching employee name for ID: {}, Error: {}", empId, e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error fetching employee name for ID: {}, Error: {}", empId, e.getMessage(), e);
            throw new RuntimeException("Failed to fetch employee name: " + e.getMessage(), e);
        }
    }

    /**
     * Helper method to map entity to response DTO
     */
    private EmployeeDocumentResponseDto mapToResponseDto(EmployeeDocument document) {
        EmployeeDocumentResponseDto responseDto = new EmployeeDocumentResponseDto();
        responseDto.setId(document.getId());
        responseDto.setEmpId(document.getEmpId());
        responseDto.setEmpName(document.getEmpName());
        responseDto.setFileName(document.getFileName());
        responseDto.setFileSize(document.getFileSize());
        responseDto.setFileUrl(document.getFileUrl());
        responseDto.setNote(document.getNote());
        responseDto.setCreatedAt(document.getCreatedAt());
        responseDto.setUpdatedAt(document.getUpdatedAt());
        responseDto.setUpdatedBy(document.getUpdatedBy());
        return responseDto;
    }
}
