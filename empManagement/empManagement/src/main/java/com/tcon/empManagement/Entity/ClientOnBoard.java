package com.tcon.empManagement.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "client_onboard")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientOnBoard {

    @Id
    private String id;

    private String projectId;

    private ClientInfo clientInfo;

    private ProjectType projectType;

    private Technical technical;

    private UiUx uiux;

    private List<FileUpload> fileUploads;

    private String description;

    private String note;

    private ContactInfo contactInfo;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // Nested Classes
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ClientInfo {
        private String businessName;
        private String projectName;
        private String clientAddress;
        private String businessPhoneNo;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProjectType {
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
    public static class Technical {
        private List<String> preferredStack;
        private String dbChoice;
        private String hosting;
        private String frontend;
        private String backend;
        private String frameworks;
        private String deployModel;
        private String releaseStrategy;
        private String supportSla;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UiUx {
        private String brandColors;
        private Boolean hasWireframes;
        private Boolean responsive;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FileUpload {
        private String fileName;
        private String fileType;
        private String fileUrl;
        private Long fileSize;
        private LocalDateTime uploadedAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ContactInfo {
        private String contactName;
        private String contactNumber;
        private String contactEmail;
        private String address;
    }
}
