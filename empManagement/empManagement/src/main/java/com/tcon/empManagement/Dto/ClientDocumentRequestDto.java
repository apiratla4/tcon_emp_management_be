package com.tcon.empManagement.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDocumentRequestDto {
    private String businessName;
    private String projectName;
    private String contactName;
    private String fileName;
    private Long fileSize;
    private String fileType;
    private String fileUrl;
    private String note;
    private String updatedBy;
    private String gcsKey;
    private MultipartFile file; // for file upload support
}
