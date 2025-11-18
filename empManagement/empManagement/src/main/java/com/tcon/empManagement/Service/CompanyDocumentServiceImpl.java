/*
package com.tcon.empManagement.Service;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.tcon.empManagement.Dto.CompanyDocumentRequestDto;
import com.tcon.empManagement.Dto.CompanyDocumentResponseDto;
import com.tcon.empManagement.Entity.CompanyDocument;
import com.tcon.empManagement.Repository.CompanyDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyDocumentServiceImpl implements CompanyDocumentService {

    private static final Logger logger = LoggerFactory.getLogger(CompanyDocumentServiceImpl.class);

    private final CompanyDocumentRepository documentRepository;

    @Autowired
    @Qualifier("serviceAccountStorage")
    private Storage storage;

    @Value("${gcs.bucket.name}")
    private String bucketName;

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
            document.setGcsKey(requestDto.getGcsKey());
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
    public CompanyDocumentResponseDto uploadCompanyDocument(MultipartFile file, CompanyDocumentRequestDto requestDto) {
        String originalFilename = file.getOriginalFilename();
        String blobName = UUID.randomUUID() + "-" + originalFilename;

        BlobId blobId = BlobId.of(bucketName, blobName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .build();

        try {
            storage.create(blobInfo, file.getInputStream());
            String fileUrl = String.format("https://storage.googleapis.com/%s/%s", bucketName, blobName);

            CompanyDocument document = new CompanyDocument();
            document.setCompanyName(requestDto.getCompanyName());
            document.setOrganizationId(requestDto.getOrganizationId());
            document.setDateOfIssue(requestDto.getDateOfIssue());
            document.setExpiryDate(requestDto.getExpiryDate());
            document.setResponsibleParty(requestDto.getResponsibleParty());
            document.setDocumentName(requestDto.getDocumentName());
            document.setFileName(originalFilename);
            document.setFileSize(file.getSize());
            document.setFileType(file.getContentType());
            document.setFileUrl(fileUrl);
            document.setGcsKey(blobName);
            document.setNote(requestDto.getNote());
            document.setCreatedAt(LocalDateTime.now());
            document.setUpdatedAt(LocalDateTime.now());
            document.setUpdatedBy(requestDto.getUpdatedBy());

            CompanyDocument saved = documentRepository.save(document);
            logger.info("File uploaded for company document: {}", saved.getId());
            return mapToResponseDto(saved);

        } catch (IOException e) {
            logger.error("Failed to upload file: {} | Error: {}", originalFilename, e.getMessage(), e);
            throw new RuntimeException("Failed to upload file: " + originalFilename, e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompanyDocumentResponseDto> getAllDocuments() {
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
    @Transactional(readOnly = true)
    public List<CompanyDocumentResponseDto> getDocumentsByOrganizationId(String organizationId) {
        try {
            return documentRepository.findByOrganizationId(organizationId)
                    .stream()
                    .map(this::mapToResponseDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error getting documents by organizationId {}: {}", organizationId, e.getMessage(), e);
            throw new RuntimeException("Failed to get documents by organizationId: " + organizationId, e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompanyDocumentResponseDto> getDocumentsByCompanyName(String companyName) {
        try {
            return documentRepository.findByCompanyName(companyName)
                    .stream()
                    .map(this::mapToResponseDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error getting documents by companyName {}: {}", companyName, e.getMessage(), e);
            throw new RuntimeException("Failed to get documents by companyName: " + companyName, e);
        }
    }

    @Override
    public String generateDownloadUrlByDocumentId(String documentId) {
        try {
            CompanyDocument doc = documentRepository.findById(documentId)
                    .orElseThrow(() -> new RuntimeException("Document not found: " + documentId));
            if (doc.getGcsKey() == null || doc.getGcsKey().trim().isEmpty()) {
                throw new RuntimeException("Storage key (gcsKey) is missing for document: " + documentId);
            }
            String url = generateDownloadUrl(doc.getGcsKey());
            logger.info("Generated download URL for document: {}", documentId);
            return url;
        } catch (Exception e) {
            logger.error("Error generating download URL for documentId {}: {}", documentId, e.getMessage(), e);
            throw new RuntimeException("Failed to generate download URL: " + documentId, e);
        }
    }

    @Override
    public String generateDownloadUrl(String gcsKey) {
        try {
            if (gcsKey == null || gcsKey.trim().isEmpty()) {
                throw new IllegalArgumentException("GCS storage key must not be null/empty!");
            }
            BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, gcsKey).build();
            URL signedUrl = storage.signUrl(blobInfo, 1, TimeUnit.MINUTES, Storage.SignUrlOption.withV4Signature());
            logger.info("Generated signed URL for blob: {}", gcsKey);
            return signedUrl.toString();
        } catch (Exception e) {
            logger.error("Error generating signed URL for blob {}: {}", gcsKey, e.getMessage(), e);
            throw new RuntimeException("Failed to generate signed URL for blob: " + gcsKey, e);
        }
    }

    @Override
    @Transactional
    public void deleteDocumentById(String id) {
        try {
            CompanyDocument doc = documentRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Company document not found with ID: " + id));
            documentRepository.deleteById(id);
            logger.info("Deleted company document from DB: {}", id);
            if (doc.getGcsKey() != null && !doc.getGcsKey().trim().isEmpty()) {
                boolean deleted = storage.delete(BlobId.of(bucketName, doc.getGcsKey()));
                logger.info("Deleted blob from GCS: {} | Deleted: {}", doc.getGcsKey(), deleted);
            }
        } catch (Exception e) {
            logger.error("Error deleting company document {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Failed to delete company document: " + id, e);
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
        dto.setFileSizeDisplay(formatFileSize(document.getFileSize())); // User-friendly size!
        dto.setFileType(document.getFileType());
        dto.setFileUrl(document.getFileUrl());
        dto.setGcsKey(document.getGcsKey());
        dto.setNote(document.getNote());
        dto.setCreatedAt(document.getCreatedAt());
        dto.setUpdatedAt(document.getUpdatedAt());
        dto.setUpdatedBy(document.getUpdatedBy());
        return dto;
    }

    private String formatFileSize(Long sizeBytes) {
        if (sizeBytes == null) return "0 B";
        double size = sizeBytes;
        String[] units = {"B", "KB", "MB", "GB", "TB"};
        int unitIndex = 0;
        while (size >= 1024 && unitIndex < units.length - 1) {
            size /= 1024;
            unitIndex++;
        }
        return String.format("%.2f %s", size, units[unitIndex]);
    }
}
*/
