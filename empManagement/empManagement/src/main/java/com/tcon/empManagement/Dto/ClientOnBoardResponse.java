package com.tcon.empManagement.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientOnBoardResponse {

    private String id;

    private String projectId;

    private ClientOnBoardCreateRequest.ClientInfoDto clientInfo;

    private ClientOnBoardCreateRequest.ProjectTypeDto projectType;

    private ClientOnBoardCreateRequest.TechnicalDto technical;

    private ClientOnBoardCreateRequest.UiUxDto uiux;

    private List<FileUploadResponseDto> fileUploads;

    private String description;
    private String status;
    private String note;

    private ClientOnBoardCreateRequest.ContactInfoDto contactInfo;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FileUploadResponseDto {
        private String fileName;
        private String fileType;
        private String fileUrl;
        private Long fileSize;
        private LocalDateTime uploadedAt;
    }
}
