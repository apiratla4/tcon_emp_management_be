package com.tcon.empManagement.Service;

import com.tcon.empManagement.Dto.CompanyDocumentRequestDto;
import com.tcon.empManagement.Dto.CompanyDocumentResponseDto;
import com.tcon.empManagement.Entity.CompanyDocument;
import com.tcon.empManagement.Repository.CompanyDocumentRepository;
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
public class CompanyDocumentServiceImpl implements CompanyDocumentService {

    private static final Logger logger = LoggerFactory.getLogger(CompanyDocumentServiceImpl.class);

    private final CompanyDocumentRepository documentRepository;

    @Override
    @Transactional
    public CompanyDocumentResponseDto createDocument(CompanyDocumentRequestDto requestDto) {
        logger.info("Creating company document for company: {}, document: {}", requestDto.getCompanyName(), requestDto.getDocumentName());
        try {
            CompanyDocument document = new CompanyDocument();
            document.setCompanyName(requestDto.getCompanyName());
            document.setOrganizationId(requestDto.getOrganizationId());
            document.setDateOfIssue(requestDto.getDateOfIssue());
            document.setExpiryDate(requestDto.getExpiryDate());
            document.setResponsibleParty(requestDto.getResponsibleParty());
            document.setDocumentName(requestDto.getDocumentName());
            document.setFileName(requestDto.getFileName());
            document.setFileSize(requestDto.getFileSize());
            document.setFileType(requestDto.getFileType());
            document.setFileUrl(requestDto.getFileUrl());
            document.setNote(requestDto.getNote());
            document.setCreatedAt(LocalDateTime.now());
            document.setUpdatedAt(LocalDateTime.now());
            document.setUpdatedBy(requestDto.getUpdatedBy());

            CompanyDocument saved = documentRepository.save(document);
            logger.info("Company document created successfully with ID: {}", saved.getId());
            return mapToResponseDto(saved);
        } catch (Exception e) {
            logger.error("Error creating company document: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create company document: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public CompanyDocumentResponseDto updateDocument(String id, CompanyDocumentRequestDto requestDto) {
        logger.info("Updating company document with ID: {}", id);
        try {
            CompanyDocument document = documentRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Company document not found with ID: " + id));

            if (requestDto.getCompanyName() != null) document.setCompanyName(requestDto.getCompanyName());
            if (requestDto.getOrganizationId() != null) document.setOrganizationId(requestDto.getOrganizationId());
            if (requestDto.getDateOfIssue() != null) document.setDateOfIssue(requestDto.getDateOfIssue());
            if (requestDto.getExpiryDate() != null) document.setExpiryDate(requestDto.getExpiryDate());
            if (requestDto.getResponsibleParty() != null) document.setResponsibleParty(requestDto.getResponsibleParty());
            if (requestDto.getDocumentName() != null) document.setDocumentName(requestDto.getDocumentName());
            if (requestDto.getFileName() != null) document.setFileName(requestDto.getFileName());
            if (requestDto.getFileSize() != null) document.setFileSize(requestDto.getFileSize());
            if (requestDto.getFileType() != null) document.setFileType(requestDto.getFileType());
            if (requestDto.getFileUrl() != null) document.setFileUrl(requestDto.getFileUrl());
            if (requestDto.getNote() != null) document.setNote(requestDto.getNote());
            document.setUpdatedAt(LocalDateTime.now());
            document.setUpdatedBy(requestDto.getUpdatedBy());

            CompanyDocument updated = documentRepository.save(document);
            logger.info("Company document updated with ID: {}", updated.getId());
            return mapToResponseDto(updated);
        } catch (Exception e) {
            logger.error("Error updating company document: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update company document: " + e.getMessage(), e);
        }
    }

    @Override
    public CompanyDocumentResponseDto getDocumentById(String id) {
        logger.info("Getting company document by ID: {}", id);
        try {
            CompanyDocument document = documentRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Company document not found with ID: " + id));
            return mapToResponseDto(document);
        } catch (Exception e) {
            logger.error("Error getting company document: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to get company document: " + e.getMessage(), e);
        }
    }

    @Override
    public List<CompanyDocumentResponseDto> getAllDocuments() {
        logger.info("Getting all company documents");
        try {
            return documentRepository.findAll().stream()
                    .map(this::mapToResponseDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error getting all company documents: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to get all company documents: " + e.getMessage(), e);
        }
    }

    @Override
    public List<CompanyDocumentResponseDto> getDocumentsByOrganizationId(String organizationId) {
        logger.info("Getting company documents by organizationId: {}", organizationId);
        try {
            return documentRepository.findByOrganizationId(organizationId)
                    .stream()
                    .map(this::mapToResponseDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error getting company documents by organizationId: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to get company documents: " + e.getMessage(), e);
        }
    }

    @Override
    public List<CompanyDocumentResponseDto> getDocumentsByCompanyName(String companyName) {
        logger.info("Getting company documents by companyName: {}", companyName);
        try {
            return documentRepository.findByCompanyName(companyName)
                    .stream()
                    .map(this::mapToResponseDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error getting company documents by companyName: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to get company documents: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void deleteDocumentById(String id) {
        logger.info("Deleting company document by ID: {}", id);
        try {
            documentRepository.deleteById(id);
            logger.info("Deleted company document by ID: {}", id);
        } catch (Exception e) {
            logger.error("Error deleting company document: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to delete company document: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void deleteDocumentsByOrganizationId(String organizationId) {
        logger.info("Deleting company documents by organizationId: {}", organizationId);
        try {
            documentRepository.deleteByOrganizationId(organizationId);
            logger.info("Deleted company documents by organizationId: {}", organizationId);
        } catch (Exception e) {
            logger.error("Error deleting company documents by organizationId: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to delete company documents: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void deleteDocumentsByCompanyName(String companyName) {
        logger.info("Deleting company documents by companyName: {}", companyName);
        try {
            List<CompanyDocument> docs = documentRepository.findByCompanyName(companyName);
            docs.forEach(doc -> documentRepository.deleteById(doc.getId()));
            logger.info("Deleted company documents by companyName: {}", companyName);
        } catch (Exception e) {
            logger.error("Error deleting company documents by companyName: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to delete company documents: " + e.getMessage(), e);
        }
    }

    private CompanyDocumentResponseDto mapToResponseDto(CompanyDocument document) {
        CompanyDocumentResponseDto dto = new CompanyDocumentResponseDto();
        dto.setId(document.getId());
        dto.setCompanyName(document.getCompanyName());
        dto.setOrganizationId(document.getOrganizationId());
        dto.setDateOfIssue(document.getDateOfIssue());
        dto.setExpiryDate(document.getExpiryDate());
        dto.setResponsibleParty(document.getResponsibleParty());
        dto.setDocumentName(document.getDocumentName());
        dto.setFileName(document.getFileName());
        dto.setFileSize(document.getFileSize());
        dto.setFileType(document.getFileType());
        dto.setFileUrl(document.getFileUrl());
        dto.setNote(document.getNote());
        dto.setCreatedAt(document.getCreatedAt());
        dto.setUpdatedAt(document.getUpdatedAt());
        dto.setUpdatedBy(document.getUpdatedBy());
        return dto;
    }
}
