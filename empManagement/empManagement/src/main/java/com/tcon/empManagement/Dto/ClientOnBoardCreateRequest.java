package com.tcon.empManagement.Dto;

import lombok.*;
import java.time.Instant;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ClientOnBoardCreateRequest {
    private String projectId;
    private ClientInfo clientInfo;
    private ProjectType projectType;
    private Website website;
    private Ecom ecom;
    private Mobile mobile;
    private Seo seo;
    private Content content;
    private Marketing marketing;
    private Technical technical;
    private UiUx uiux;

    // Nested DTOs (same shape as entity for simplicity)
    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class ClientInfo {
        private String businessName;
        private String stakeholders;
        private String projectName;
        private Integer budget;
        private Integer timelineWeeks;
    }
    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class ProjectType {
        private String features;
        private String userRoles;
        private String integrations;
        private String featuresCsv;
        private String userRolesCsv;
        private String integrationsCsv;
    }
    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Website {
        private Section header;
        private Section footer;
        private Section home;
        private Section auth;
        private Section settings;
        @Data @NoArgsConstructor @AllArgsConstructor @Builder
        public static class Section {
            private List<String> items;
            private String notes;
        }
    }
    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Ecom {
        private String variants;
        private String taxRegions;
        private String gateways;
        private String shipping;
    }
    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Mobile {
        private String targets;
        private String approach;
        private String push;
        private String offline;
    }
    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Seo {
        private String pages;
        private String schema;
        private String kpis;
        private String cadence;
    }
    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Content {
        private String types;
        private String tone;
    }
    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Marketing {
        private String channels;
        private String split;
    }
    @Data @NoArgsConstructor @AllArgsConstructor @Builder
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
    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class UiUx {
        private String brandColors;
        private Boolean hasWireframes;
        private Boolean responsive;
    }
}
