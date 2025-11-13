package com.tcon.empManagement.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDocumentRequestDto {

    private String businessName;
    private String projectName;
    private String contactName; // from ClientOnBoard
    private String fileName;
    private Long fileSize;
    private String fileType;
    private String fileUrl;
    private String note;
    private String updatedBy;
}
