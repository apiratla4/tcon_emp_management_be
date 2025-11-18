package com.tcon.empManagement.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientOnBoardCreateRequest {

    @NotBlank(message = "Project ID is required")
    private String projectId;

    @NotNull(message = "Client info is required")
    private ClientInfoDto clientInfo;

    private ProjectTypeDto projectType;

    private TechnicalDto technical;

    private UiUxDto uiux;

    private List<FileUploadDto> fileUploads;

    private String description;

    private String note;
    private String status;
    @NotNull(message = "Contact info is required")
    private ContactInfoDto contactInfo;

    // Nested DTOs
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ClientInfoDto {
        private String businessName;
        private String projectName;
        private String clientAddress;
        private String businessPhoneNo;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProjectTypeDto {
        private String websiteDev;
        private String ecommerceApp;
        private String mobileApp;
        private String seoServices;
        private String contentManagement;
        private String digitalMarketing;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TechnicalDto {
        private List<String> preferredStack;
        private String dbChoice;
        private String hosting;
        private String frontend;
        private String backend;
        private String frameworks;
        private String deployModel;
        private String releaseStrategy;
        private String supportSla;
        private String description;
        private String note;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UiUxDto {
        private String brandColors;
        private Boolean hasWireframes;
        private Boolean responsive;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FileUploadDto {
        private String fileName;
        private String fileType;
        private String fileUrl;
        private Long fileSize;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ContactInfoDto {
        private String contactName;
        private String contactNumber;
        private String contactEmail;
        private String address;
    }
}
