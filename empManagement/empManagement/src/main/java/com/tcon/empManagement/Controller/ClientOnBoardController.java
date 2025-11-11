package com.tcon.empManagement.Controller;

import com.tcon.empManagement.Dto.ClientOnBoardCreateRequest;
import com.tcon.empManagement.Dto.ClientOnBoardResponse;
import com.tcon.empManagement.Dto.ClientOnBoardUpdateRequest;
import com.tcon.empManagement.Service.ClientOnBoardService;
import com.tcon.empManagement.Service.FileUploadService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/client-onboard")
@CrossOrigin(origins = "*")
@Slf4j
public class ClientOnBoardController {

    @Autowired
    private ClientOnBoardService clientOnBoardService;

    @Autowired
    private FileUploadService fileUploadService;

    // ============ CREATE WITH FILE UPLOAD ============

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ClientOnBoardResponse> createClientOnBoardWithFiles(
            @RequestPart("data") @Valid ClientOnBoardCreateRequest request,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) {

        log.info("Creating client onboard for project: {}", request.getProjectId());

        try {
            // Upload files if provided
            if (files != null && !files.isEmpty()) {
                log.info("Uploading {} files", files.size());
                List<ClientOnBoardCreateRequest.FileUploadDto> fileUploadDtos = new ArrayList<>();

                for (MultipartFile file : files) {
                    String fileName = fileUploadService.uploadFile(file);

                    ClientOnBoardCreateRequest.FileUploadDto fileDto = ClientOnBoardCreateRequest.FileUploadDto.builder()
                            .fileName(fileName)
                            .fileType(file.getContentType())
                            .fileUrl("/api/files/download/" + fileName)
                            .fileSize(file.getSize())
                            .build();

                    fileUploadDtos.add(fileDto);
                }

                request.setFileUploads(fileUploadDtos);
            }

            ClientOnBoardResponse response = clientOnBoardService.createClientOnBoard(request);
            log.info("Client onboard created successfully: {}", response.getId());
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e) {
            log.error("Error creating client onboard: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create client onboard: " + e.getMessage());
        }
    }

    // ============ CREATE WITHOUT FILE UPLOAD (JSON ONLY) ============

    @PostMapping(value = "/json", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientOnBoardResponse> createClientOnBoard(@Valid @RequestBody ClientOnBoardCreateRequest request) {
        log.info("Creating client onboard (JSON only) for project: {}", request.getProjectId());

        try {
            ClientOnBoardResponse response = clientOnBoardService.createClientOnBoard(request);
            log.info("Client onboard created successfully: {}", response.getId());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error creating client onboard: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create client onboard: " + e.getMessage());
        }
    }

    // ============ UPDATE WITH FILE UPLOAD ============

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ClientOnBoardResponse> updateClientOnBoardWithFiles(
            @PathVariable String id,
            @RequestPart("data") ClientOnBoardUpdateRequest request,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) {

        log.info("Updating client onboard: {}", id);

        try {
            // Upload new files if provided
            if (files != null && !files.isEmpty()) {
                log.info("Uploading {} new files", files.size());
                List<ClientOnBoardCreateRequest.FileUploadDto> fileUploadDtos = new ArrayList<>();

                for (MultipartFile file : files) {
                    String fileName = fileUploadService.uploadFile(file);

                    ClientOnBoardCreateRequest.FileUploadDto fileDto = ClientOnBoardCreateRequest.FileUploadDto.builder()
                            .fileName(fileName)
                            .fileType(file.getContentType())
                            .fileUrl("/api/files/download/" + fileName)
                            .fileSize(file.getSize())
                            .build();

                    fileUploadDtos.add(fileDto);
                }

                request.setFileUploads(fileUploadDtos);
            }

            ClientOnBoardResponse response = clientOnBoardService.updateClientOnBoard(id, request);
            log.info("Client onboard updated successfully: {}", id);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error updating client onboard: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update client onboard: " + e.getMessage());
        }
    }

    // ============ UPDATE WITHOUT FILE UPLOAD (JSON ONLY) ============

    @PutMapping(value = "/{id}/json", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientOnBoardResponse> updateClientOnBoard(
            @PathVariable String id,
            @RequestBody ClientOnBoardUpdateRequest request) {
        log.info("Updating client onboard (JSON only): {}", id);

        try {
            ClientOnBoardResponse response = clientOnBoardService.updateClientOnBoard(id, request);
            log.info("Client onboard updated successfully: {}", id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error updating client onboard: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update client onboard: " + e.getMessage());
        }
    }

    // ============ ADD FILES TO EXISTING CLIENT ONBOARD ============

    @PostMapping("/{id}/add-files")
    public ResponseEntity<Map<String, Object>> addFilesToClientOnBoard(
            @PathVariable String id,
            @RequestParam("files") List<MultipartFile> files) {

        log.info("Adding files to client onboard: {}", id);

        try {
            // Validate files
            if (files == null || files.isEmpty()) {
                log.warn("No files provided for upload");
                return ResponseEntity.badRequest()
                        .body(Map.of("success", false, "message", "No files provided"));
            }

            // Get existing client onboard
            ClientOnBoardResponse existing = clientOnBoardService.getClientOnBoardById(id);

            // Upload new files
            List<ClientOnBoardCreateRequest.FileUploadDto> newFiles = new ArrayList<>();
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    String fileName = fileUploadService.uploadFile(file);

                    ClientOnBoardCreateRequest.FileUploadDto fileDto = ClientOnBoardCreateRequest.FileUploadDto.builder()
                            .fileName(fileName)
                            .fileType(file.getContentType())
                            .fileUrl("/api/files/download/" + fileName)
                            .fileSize(file.getSize())
                            .build();

                    newFiles.add(fileDto);
                }
            }

            // Merge with existing files
            List<ClientOnBoardCreateRequest.FileUploadDto> allFiles = new ArrayList<>();
            if (existing.getFileUploads() != null) {
                // Convert existing files to DTO
                for (ClientOnBoardResponse.FileUploadResponseDto existingFile : existing.getFileUploads()) {
                    ClientOnBoardCreateRequest.FileUploadDto dto = ClientOnBoardCreateRequest.FileUploadDto.builder()
                            .fileName(existingFile.getFileName())
                            .fileType(existingFile.getFileType())
                            .fileUrl(existingFile.getFileUrl())
                            .fileSize(existingFile.getFileSize())
                            .build();
                    allFiles.add(dto);
                }
            }
            allFiles.addAll(newFiles);

            // Update client onboard with all files
            ClientOnBoardUpdateRequest updateRequest = new ClientOnBoardUpdateRequest();
            updateRequest.setFileUploads(allFiles);
            clientOnBoardService.updateClientOnBoard(id, updateRequest);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Files added successfully");
            response.put("filesAdded", newFiles.size());
            response.put("totalFiles", allFiles.size());
            response.put("newFileNames", newFiles.stream()
                    .map(ClientOnBoardCreateRequest.FileUploadDto::getFileName)
                    .toList());

            log.info("Files added successfully to client onboard: {}", id);
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            log.error("Error adding files to client onboard: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "message", e.getMessage()));
        } catch (Exception e) {
            log.error("Unexpected error adding files to client onboard: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Failed to add files: " + e.getMessage()));
        }
    }

    // ============ GET OPERATIONS ============

    @GetMapping("/{id}")
    public ResponseEntity<ClientOnBoardResponse> getClientOnBoardById(@PathVariable String id) {
        log.info("Fetching client onboard: {}", id);

        try {
            ClientOnBoardResponse response = clientOnBoardService.getClientOnBoardById(id);
            log.info("Client onboard fetched successfully: {}", id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Client onboard not found: {}", id);
            throw e;
        }
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<ClientOnBoardResponse> getClientOnBoardByProjectId(@PathVariable String projectId) {
        log.info("Fetching client onboard by project ID: {}", projectId);

        try {
            ClientOnBoardResponse response = clientOnBoardService.getClientOnBoardByProjectId(projectId);
            log.info("Client onboard fetched successfully for project: {}", projectId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Client onboard not found for project: {}", projectId);
            throw e;
        }
    }

    @GetMapping
    public ResponseEntity<List<ClientOnBoardResponse>> getAllClientOnBoards() {
        log.info("Fetching all client onboards");

        try {
            List<ClientOnBoardResponse> responses = clientOnBoardService.getAllClientOnBoards();
            log.info("Fetched {} client onboards", responses.size());
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            log.error("Error fetching all client onboards: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch client onboards: " + e.getMessage());
        }
    }

    @GetMapping("/business/{businessName}")
    public ResponseEntity<List<ClientOnBoardResponse>> getClientOnBoardsByBusinessName(@PathVariable String businessName) {
        log.info("Fetching client onboards by business name: {}", businessName);

        try {
            List<ClientOnBoardResponse> responses = clientOnBoardService.getClientOnBoardsByBusinessName(businessName);
            log.info("Fetched {} client onboards for business: {}", responses.size(), businessName);
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            log.error("Error fetching client onboards by business name: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch client onboards: " + e.getMessage());
        }
    }

    @GetMapping("/email/{contactEmail}")
    public ResponseEntity<List<ClientOnBoardResponse>> getClientOnBoardsByContactEmail(@PathVariable String contactEmail) {
        log.info("Fetching client onboards by contact email: {}", contactEmail);

        try {
            List<ClientOnBoardResponse> responses = clientOnBoardService.getClientOnBoardsByContactEmail(contactEmail);
            log.info("Fetched {} client onboards for email: {}", responses.size(), contactEmail);
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            log.error("Error fetching client onboards by email: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch client onboards: " + e.getMessage());
        }
    }

    // ============ DELETE OPERATIONS ============

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteClientOnBoard(@PathVariable String id) {
        log.info("Deleting client onboard: {}", id);

        try {
            // Get client onboard to delete associated files
            ClientOnBoardResponse existing = clientOnBoardService.getClientOnBoardById(id);

            // Delete associated files
            int filesDeleted = 0;
            if (existing.getFileUploads() != null) {
                for (ClientOnBoardResponse.FileUploadResponseDto file : existing.getFileUploads()) {
                    boolean deleted = fileUploadService.deleteFile(file.getFileName());
                    if (deleted) {
                        filesDeleted++;
                    }
                }
            }

            // Delete client onboard
            clientOnBoardService.deleteClientOnBoard(id);

            log.info("Client onboard deleted successfully: {}. Files deleted: {}", id, filesDeleted);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Client onboard deleted successfully",
                    "filesDeleted", filesDeleted
            ));

        } catch (RuntimeException e) {
            log.error("Error deleting client onboard: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "message", e.getMessage()));
        } catch (Exception e) {
            log.error("Unexpected error deleting client onboard: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Failed to delete client onboard: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}/file/{fileName}")
    public ResponseEntity<Map<String, Object>> deleteFileFromClientOnBoard(
            @PathVariable String id,
            @PathVariable String fileName) {

        log.info("Deleting file {} from client onboard: {}", fileName, id);

        try {
            // Get existing client onboard
            ClientOnBoardResponse existing = clientOnBoardService.getClientOnBoardById(id);

            // Check if file exists
            boolean fileExists = false;
            if (existing.getFileUploads() != null) {
                fileExists = existing.getFileUploads().stream()
                        .anyMatch(f -> f.getFileName().equals(fileName));
            }

            if (!fileExists) {
                log.warn("File not found in client onboard: {}", fileName);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("success", false, "message", "File not found in client onboard"));
            }

            // Remove file from list
            List<ClientOnBoardCreateRequest.FileUploadDto> remainingFiles = new ArrayList<>();
            if (existing.getFileUploads() != null) {
                for (ClientOnBoardResponse.FileUploadResponseDto file : existing.getFileUploads()) {
                    if (!file.getFileName().equals(fileName)) {
                        ClientOnBoardCreateRequest.FileUploadDto dto = ClientOnBoardCreateRequest.FileUploadDto.builder()
                                .fileName(file.getFileName())
                                .fileType(file.getFileType())
                                .fileUrl(file.getFileUrl())
                                .fileSize(file.getFileSize())
                                .build();
                        remainingFiles.add(dto);
                    }
                }
            }

            // Delete physical file
            boolean deleted = fileUploadService.deleteFile(fileName);

            // Update client onboard
            ClientOnBoardUpdateRequest updateRequest = new ClientOnBoardUpdateRequest();
            updateRequest.setFileUploads(remainingFiles);
            clientOnBoardService.updateClientOnBoard(id, updateRequest);

            log.info("File deleted successfully from client onboard. Physical file deleted: {}", deleted);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "File deleted successfully",
                    "remainingFiles", remainingFiles.size()
            ));

        } catch (RuntimeException e) {
            log.error("Error deleting file from client onboard: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "message", e.getMessage()));
        } catch (Exception e) {
            log.error("Unexpected error deleting file from client onboard: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Failed to delete file: " + e.getMessage()));
        }
    }

    // ============ GET FILES COUNT ============

    @GetMapping("/{id}/files/count")
    public ResponseEntity<Map<String, Object>> getFilesCount(@PathVariable String id) {
        log.info("Getting files count for client onboard: {}", id);

        try {
            ClientOnBoardResponse existing = clientOnBoardService.getClientOnBoardById(id);
            int filesCount = existing.getFileUploads() != null ? existing.getFileUploads().size() : 0;

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("clientOnboardId", id);
            response.put("filesCount", filesCount);

            log.info("Files count for client onboard {}: {}", id, filesCount);
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            log.error("Error getting files count: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }
}
