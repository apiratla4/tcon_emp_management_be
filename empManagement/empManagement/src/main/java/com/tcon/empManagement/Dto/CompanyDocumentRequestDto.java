package com.tcon.empManagement.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDocumentRequestDto {

    private String companyName;
    private String organizationId;
    private LocalDateTime dateOfIssue;
    private LocalDateTime expiryDate;
    private String responsibleParty;
    private String documentName;
    private String fileName;
    private Long fileSize;
    private String fileType;
    private String fileUrl;
    private String note;
    private String updatedBy; // employeeId
}
