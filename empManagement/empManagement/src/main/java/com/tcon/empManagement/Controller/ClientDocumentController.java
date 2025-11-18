package com.tcon.empManagement.Controller;

import com.tcon.empManagement.Dto.ClientDocumentRequestDto;
import com.tcon.empManagement.Dto.ClientDocumentResponseDto;
import com.tcon.empManagement.Service.ClientDocumentService;
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
@RequestMapping("/api/client-documents")
public class ClientDocumentController {

    private static final Logger logger = LoggerFactory.getLogger(ClientDocumentController.class);

    private final ClientDocumentService clientDocumentService;

    @PostMapping
    public ResponseEntity<ClientDocumentResponseDto> createDocument(@RequestBody ClientDocumentRequestDto requestDto) {
        try {
            ClientDocumentResponseDto dto = clientDocumentService.createDocument(requestDto);
            logger.info("Client document created: {}", dto.getId());
            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error creating client document: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<ClientDocumentResponseDto> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @ModelAttribute ClientDocumentRequestDto requestDto) {
        try {
            ClientDocumentResponseDto dto = clientDocumentService.uploadClientDocument(file, requestDto);
            logger.info("File uploaded for client document: {}", dto.getId());
            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error uploading client document file: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ClientDocumentResponseDto>> getAllDocuments() {
        try {
            List<ClientDocumentResponseDto> docs = clientDocumentService.getAllDocuments();
            return ResponseEntity.ok(docs);
        } catch (Exception e) {
            logger.error("Error getting all client documents: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/download-url/{documentId}")
    public ResponseEntity<String> getDownloadUrl(@PathVariable String documentId) {
        try {
            String url = clientDocumentService.generateDownloadUrlByDocumentId(documentId);
            logger.info("Generated signed download URL for client document: {}", documentId);
            return ResponseEntity.ok(url);
        } catch (Exception e) {
            logger.error("Error generating download URL for client documentId {}: {}", documentId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
