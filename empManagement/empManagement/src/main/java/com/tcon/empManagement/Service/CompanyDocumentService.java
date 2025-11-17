package com.tcon.empManagement.Service;

import com.tcon.empManagement.Dto.CompanyDocumentRequestDto;
import com.tcon.empManagement.Dto.CompanyDocumentResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CompanyDocumentService {

    CompanyDocumentResponseDto createDocument(CompanyDocumentRequestDto requestDto);

    CompanyDocumentResponseDto uploadCompanyDocument(MultipartFile file, CompanyDocumentRequestDto requestDto);

    List<CompanyDocumentResponseDto> getAllDocuments();

    List<CompanyDocumentResponseDto> getDocumentsByOrganizationId(String organizationId);

    List<CompanyDocumentResponseDto> getDocumentsByCompanyName(String companyName);

    String generateDownloadUrlByDocumentId(String documentId);

    String generateDownloadUrl(String gcsKey);

    void deleteDocumentById(String id);
}
