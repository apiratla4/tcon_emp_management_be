package com.tcon.empManagement.Service;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.tcon.empManagement.Dto.ClientDocumentRequestDto;
import com.tcon.empManagement.Dto.ClientDocumentResponseDto;
import com.tcon.empManagement.Entity.ClientDocument;
import com.tcon.empManagement.Repository.ClientDocumentRepository;
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
public class ClientDocumentServiceImpl implements ClientDocumentService {

    private static final Logger logger = LoggerFactory.getLogger(ClientDocumentServiceImpl.class);

    private final ClientDocumentRepository documentRepository;

    @Autowired
    @Qualifier("serviceAccountStorage")
    private Storage storage;

    @Value("${gcs.bucket.name}")
    private String bucketName;

    @Override
    @Transactional
    public ClientDocumentResponseDto createDocument(ClientDocumentRequestDto requestDto) {
        logger.info("Creating client document for business: {}, project: {}", requestDto.getBusinessName(), requestDto.getProjectName());
        try {
            ClientDocument document = new ClientDocument();
            document.setBusinessName(requestDto.getBusinessName());
            document.setProjectName(requestDto.getProjectName());
            document.setContactName(requestDto.getContactName());
            document.setFileName(requestDto.getFileName());
            document.setFileSize(requestDto.getFileSize());
            document.setFileType(requestDto.getFileType());
            document.setFileUrl(requestDto.getFileUrl());
            document.setNote(requestDto.getNote());
            document.setCreatedAt(LocalDateTime.now());
            document.setUpdatedAt(LocalDateTime.now());
            document.setUpdatedBy(requestDto.getUpdatedBy());
            // Optionally add gcsKey (if present in DTO)

            ClientDocument saved = documentRepository.save(document);
            logger.info("Client document created successfully with ID: {}", saved.getId());
            return mapToResponseDto(saved);
        } catch (Exception e) {
            logger.error("Error creating client document: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create client document: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public ClientDocumentResponseDto uploadClientDocument(MultipartFile file, ClientDocumentRequestDto requestDto) {
        String originalFilename = file.getOriginalFilename();
        String blobName = UUID.randomUUID() + "-" + originalFilename;

        BlobId blobId = BlobId.of(bucketName, blobName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .build();

        try {
            storage.create(blobInfo, file.getInputStream());
            String fileUrl = String.format("https://storage.googleapis.com/%s/%s", bucketName, blobName);

            ClientDocument document = new ClientDocument();
            document.setBusinessName(requestDto.getBusinessName());
            document.setProjectName(requestDto.getProjectName());
            document.setContactName(requestDto.getContactName());
            document.setFileName(originalFilename);
            document.setFileSize(file.getSize());
            document.setFileType(file.getContentType());
            document.setFileUrl(fileUrl);
            document.setNote(requestDto.getNote());
            document.setCreatedAt(LocalDateTime.now());
            document.setUpdatedAt(LocalDateTime.now());
            document.setUpdatedBy(requestDto.getUpdatedBy());
            document.setGcsKey(blobName); // recommended field for lifecycle

            ClientDocument saved = documentRepository.save(document);
            logger.info("File uploaded for client document: {}", saved.getId());
            return mapToResponseDto(saved);

        } catch (IOException e) {
            logger.error("Failed to upload file: {} | Error: {}", originalFilename, e.getMessage(), e);
            throw new RuntimeException("Failed to upload file: " + originalFilename, e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClientDocumentResponseDto> getAllDocuments() {
        try {
            return documentRepository.findAll().stream()
                    .map(this::mapToResponseDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error getting all client documents: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to get all client documents: " + e.getMessage(), e);
        }
    }

    @Override
    public String generateDownloadUrlByDocumentId(String documentId) {
        try {
            ClientDocument doc = documentRepository.findById(documentId)
                    .orElseThrow(() -> new RuntimeException("Document not found: " + documentId));
            if (doc.getGcsKey() == null || doc.getGcsKey().trim().isEmpty()) {
                throw new RuntimeException("Storage key (gcsKey) is missing for document: " + documentId);
            }
            String url = generateDownloadUrl(doc.getGcsKey());
            logger.info("Generated download URL for client document: {}", documentId);
            return url;
        } catch (Exception e) {
            logger.error("Error generating download URL for client documentId {}: {}", documentId, e.getMessage(), e);
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
            logger.info("Generated signed URL for client blob: {}", gcsKey);
            return signedUrl.toString();
        } catch (Exception e) {
            logger.error("Error generating signed URL for client blob {}: {}", gcsKey, e.getMessage(), e);
            throw new RuntimeException("Failed to generate signed URL for client blob: " + gcsKey, e);
        }
    }

    private ClientDocumentResponseDto mapToResponseDto(ClientDocument document) {
        ClientDocumentResponseDto dto = new ClientDocumentResponseDto();
        dto.setId(document.getId());
        dto.setBusinessName(document.getBusinessName());
        dto.setProjectName(document.getProjectName());
        dto.setContactName(document.getContactName());
        dto.setFileName(document.getFileName());
        dto.setFileSize(document.getFileSize());
        dto.setFileSizeDisplay(formatFileSize(document.getFileSize())); // readable
        dto.setFileType(document.getFileType());
        dto.setFileUrl(document.getFileUrl());
        dto.setNote(document.getNote());
        dto.setCreatedAt(document.getCreatedAt());
        dto.setUpdatedAt(document.getUpdatedAt());
        dto.setUpdatedBy(document.getUpdatedBy());
        dto.setGcsKey(document.getGcsKey());
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
