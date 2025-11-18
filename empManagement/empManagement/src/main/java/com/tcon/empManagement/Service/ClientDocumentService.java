package com.tcon.empManagement.Service;

import com.tcon.empManagement.Dto.ClientDocumentRequestDto;
import com.tcon.empManagement.Dto.ClientDocumentResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ClientDocumentService {
    ClientDocumentResponseDto createDocument(ClientDocumentRequestDto requestDto);
    ClientDocumentResponseDto uploadClientDocument(MultipartFile file, ClientDocumentRequestDto requestDto);
    List<ClientDocumentResponseDto> getAllDocuments();
    String generateDownloadUrlByDocumentId(String documentId);
    String generateDownloadUrl(String gcsKey);
    // add delete and get by business/project if needed (same as previous structure)
}
