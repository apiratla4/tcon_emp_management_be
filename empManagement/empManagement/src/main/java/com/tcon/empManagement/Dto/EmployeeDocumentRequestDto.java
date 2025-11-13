package com.tcon.empManagement.Dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDocumentRequestDto {

    private String empId;
    private String fileName;
    private Long fileSize;
    private String fileUrl;
    private String note;
    private String updatedBy; // For update operations
}
