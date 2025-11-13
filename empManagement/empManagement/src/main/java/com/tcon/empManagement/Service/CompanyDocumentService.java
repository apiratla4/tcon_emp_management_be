package com.tcon.empManagement.Service;



import com.tcon.empManagement.Dto.CompanyDocumentRequestDto;
import com.tcon.empManagement.Dto.CompanyDocumentResponseDto;

import java.util.List;

public interface CompanyDocumentService {

    CompanyDocumentResponseDto createDocument(CompanyDocumentRequestDto requestDto);

    CompanyDocumentResponseDto updateDocument(String id, CompanyDocumentRequestDto requestDto);

    CompanyDocumentResponseDto getDocumentById(String id);

    List<CompanyDocumentResponseDto> getAllDocuments();

    List<CompanyDocumentResponseDto> getDocumentsByOrganizationId(String organizationId);

    List<CompanyDocumentResponseDto> getDocumentsByCompanyName(String companyName);

    void deleteDocumentById(String id);

    void deleteDocumentsByOrganizationId(String organizationId);

    void deleteDocumentsByCompanyName(String companyName);
}
