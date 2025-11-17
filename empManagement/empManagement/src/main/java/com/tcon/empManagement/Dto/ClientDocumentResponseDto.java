package com.tcon.empManagement.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDocumentResponseDto {
    private String id;
    private String businessName;
    private String projectName;
    private String contactName;
    private String fileName;
    private Long fileSize;
    private String fileSizeDisplay; // human-readable KB/MB/GB
    private String fileType;
    private String fileUrl;
    private String note;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private String gcsKey;
}
