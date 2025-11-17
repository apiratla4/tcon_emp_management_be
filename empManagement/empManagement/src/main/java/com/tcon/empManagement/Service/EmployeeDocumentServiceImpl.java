package com.tcon.empManagement.Service;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.tcon.empManagement.Dto.EmployeeDocumentCreateDto;
import com.tcon.empManagement.Dto.EmployeeDocumentResponseDto;
import com.tcon.empManagement.Entity.EmployeeDocument;
import com.tcon.empManagement.Repository.EmployeeDocumentRepository;
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
public class EmployeeDocumentServiceImpl implements EmployeeDocumentService {

    private final Storage storage;
    private final EmployeeDocumentRepository documentRepository;
    private final String bucketName;

    public EmployeeDocumentServiceImpl(
            @Qualifier("serviceAccountStorage") Storage storage,
            EmployeeDocumentRepository documentRepository,
            @Value("${gcs.bucket.name}") String bucketName) {
        this.storage = storage;
        this.documentRepository = documentRepository;
        this.bucketName = bucketName;
    }

    @Override
    @Transactional
    public EmployeeDocumentResponseDto uploadEmployeeDocument(MultipartFile file, EmployeeDocumentCreateDto createDto) {
        String originalFilename = file.getOriginalFilename();
        String blobName = UUID.randomUUID() + "-" + originalFilename;

        BlobId blobId = BlobId.of(bucketName, blobName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .build();

        try {
            storage.create(blobInfo, file.getInputStream());
            String fileUrl = String.format("https://storage.googleapis.com/%s/%s", bucketName, blobName);

            EmployeeDocument document = new EmployeeDocument();
            document.setEmpId(createDto.getEmpId());
            document.setEmpName(createDto.getEmpName());
            document.setFileName(originalFilename);
            document.setFileSize(file.getSize());
            document.setFileUrl(fileUrl);
            document.setGcsKey(blobName); // Store real GCS key!
            document.setNote(createDto.getNote());
            document.setCreatedAt(LocalDateTime.now());
            document.setUpdatedAt(LocalDateTime.now());
            document.setUpdatedBy(createDto.getUpdatedBy());

            EmployeeDocument savedDocument = documentRepository.save(document);

            return convertToResponseDto(savedDocument);

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file: " + originalFilename, e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDocumentResponseDto> listAllDocuments() {
        return documentRepository.findAll().stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDocumentResponseDto> findDocumentsByEmpId(String empId) {
        return documentRepository.findByEmpId(empId).stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public String generateDownloadUrlByDocumentId(String documentId) {
        EmployeeDocument doc = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found: " + documentId));
        if (doc.getGcsKey() == null || doc.getGcsKey().trim().isEmpty()) {
            throw new RuntimeException("Storage key (gcsKey) is missing for document: " + documentId);
        }
        return generateDownloadUrl(doc.getGcsKey());
    }

    @Override
    public String generateDownloadUrl(String gcsKey) {
        if (gcsKey == null || gcsKey.trim().isEmpty()) {
            throw new IllegalArgumentException("GCS storage key must not be null/empty!");
        }
        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, gcsKey).build();
        URL signedUrl = storage.signUrl(blobInfo, 1, TimeUnit.MINUTES, Storage.SignUrlOption.withV4Signature());
        return signedUrl.toString();
    }

    @Override
    @Transactional
    public void deleteDocument(String documentId, String updatedBy) {
        EmployeeDocument doc = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found: " + documentId));
        doc.setUpdatedAt(LocalDateTime.now());
        doc.setUpdatedBy(updatedBy);
        documentRepository.delete(doc);

        if (doc.getGcsKey() != null && !doc.getGcsKey().trim().isEmpty()) {
            storage.delete(BlobId.of(bucketName, doc.getGcsKey()));
        }
    }

    private EmployeeDocumentResponseDto convertToResponseDto(EmployeeDocument document) {
        EmployeeDocumentResponseDto dto = new EmployeeDocumentResponseDto();
        dto.setId(document.getId());
        dto.setEmpId(document.getEmpId());
        dto.setEmpName(document.getEmpName());
        dto.setFileName(document.getFileName());
        dto.setFileSize(document.getFileSize());
        dto.setFileUrl(document.getFileUrl());
        dto.setNote(document.getNote());
        dto.setCreatedAt(document.getCreatedAt());
        dto.setUpdatedAt(document.getUpdatedAt());
        dto.setUpdatedBy(document.getUpdatedBy());
        // Optionally add gcsKey if you want to show in frontend
        return dto;
    }
}
