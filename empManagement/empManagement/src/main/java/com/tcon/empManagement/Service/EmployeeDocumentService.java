package com.tcon.empManagement.Service;

import com.tcon.empManagement.Dto.EmployeeDocumentRequestDto;
import com.tcon.empManagement.Dto.EmployeeDocumentResponseDto;

import java.util.List;

public interface EmployeeDocumentService {

    EmployeeDocumentResponseDto createDocument(EmployeeDocumentRequestDto requestDto);

    EmployeeDocumentResponseDto updateDocument(String documentId, EmployeeDocumentRequestDto requestDto);

    EmployeeDocumentResponseDto getDocumentById(String documentId);

    List<EmployeeDocumentResponseDto> getAllDocuments();

    List<EmployeeDocumentResponseDto> getDocumentsByEmployeeId(String empId);

    void deleteDocument(String documentId);

    void deleteDocumentsByEmployeeId(String empId);

    // Validate if employee exists
    boolean validateEmployeeExists(String empId);
}
