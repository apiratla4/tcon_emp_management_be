package com.tcon.empManagement.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class FileUploadServiceImpl implements FileUploadService {

    @Value("${file.upload.dir:uploads}")
    private String uploadDir;

    @Override
    public String uploadFile(MultipartFile file) {
        log.info("Uploading file: {}", file.getOriginalFilename());

        try {
            // Create upload directory if it doesn't exist
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
                log.info("Created upload directory: {}", uploadPath);
            }

            // Generate unique filename
            String originalFilename = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String uniqueId = UUID.randomUUID().toString().substring(0, 8);
            String fileName = timestamp + "_" + uniqueId + fileExtension;

            // Save file
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            log.info("File uploaded successfully: {}", fileName);
            return fileName;

        } catch (IOException e) {
            log.error("Error uploading file: {}. Error: {}", file.getOriginalFilename(), e.getMessage(), e);
            throw new RuntimeException("Failed to upload file: " + file.getOriginalFilename(), e);
        }
    }

    @Override
    public List<String> uploadMultipleFiles(List<MultipartFile> files) {
        log.info("Uploading {} files", files.size());

        List<String> uploadedFiles = new ArrayList<>();

        try {
            for (MultipartFile file : files) {
                String fileName = uploadFile(file);
                uploadedFiles.add(fileName);
            }

            log.info("Successfully uploaded {} files", uploadedFiles.size());
            return uploadedFiles;

        } catch (Exception e) {
            log.error("Error uploading multiple files. Error: {}", e.getMessage(), e);
            // Delete already uploaded files in case of error
            for (String fileName : uploadedFiles) {
                deleteFile(fileName);
            }
            throw new RuntimeException("Failed to upload files", e);
        }
    }

    @Override
    public byte[] downloadFile(String fileName) {
        log.info("Downloading file: {}", fileName);

        try {
            Path filePath = Paths.get(uploadDir).resolve(fileName);

            if (!Files.exists(filePath)) {
                log.warn("File not found: {}", fileName);
                throw new RuntimeException("File not found: " + fileName);
            }

            byte[] fileContent = Files.readAllBytes(filePath);
            log.info("File downloaded successfully: {}", fileName);
            return fileContent;

        } catch (IOException e) {
            log.error("Error downloading file: {}. Error: {}", fileName, e.getMessage(), e);
            throw new RuntimeException("Failed to download file: " + fileName, e);
        }
    }

    @Override
    public boolean deleteFile(String fileName) {
        log.info("Deleting file: {}", fileName);

        try {
            Path filePath = Paths.get(uploadDir).resolve(fileName);

            if (!Files.exists(filePath)) {
                log.warn("File not found for deletion: {}", fileName);
                return false;
            }

            Files.delete(filePath);
            log.info("File deleted successfully: {}", fileName);
            return true;

        } catch (IOException e) {
            log.error("Error deleting file: {}. Error: {}", fileName, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public List<String> getAllFiles() {
        log.info("Fetching all files from upload directory");

        try {
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                log.warn("Upload directory does not exist");
                return new ArrayList<>();
            }

            try (Stream<Path> paths = Files.list(uploadPath)) {
                List<String> files = paths
                        .filter(Files::isRegularFile)
                        .map(Path::getFileName)
                        .map(Path::toString)
                        .collect(Collectors.toList());

                log.info("Found {} files in upload directory", files.size());
                return files;
            }

        } catch (IOException e) {
            log.error("Error fetching files. Error: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch files", e);
        }
    }
}
