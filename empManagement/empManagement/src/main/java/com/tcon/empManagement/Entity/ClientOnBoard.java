package com.tcon.empManagement.Entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = "client_onboard")
@Getter @Setter
@Builder
@AllArgsConstructor @NoArgsConstructor
public class ClientOnBoard {

    @Id
    private String id;

    private String projectId;

    private ClientInfo clientInfo;           // 2
    private ProjectType projectType;         // 3
    private Website website;                 // 4
    private Ecom ecom;                       // 5
    private Mobile mobile;                   // 7
    private Seo seo;                         // 8
    private Content content;                 // 9
    private Marketing marketing;             // 10
    private Technical technical;             // 11
    private UiUx uiux;                       // 12

    private Instant createdAt;               // 13
    private Instant updatedAt;               // 14

    // 2. clientInfo
    @Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
    public static class ClientInfo {
        private String businessName;
        private String stakeholders;
        private String projectName;
        private Integer budget;
        private Integer timelineWeeks;
    }

    // 3. projectType
    @Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
    public static class ProjectType {
        private String features;
        private String userRoles;
        private String integrations;
        private String featuresCsv;
        private String userRolesCsv;
        private String integrationsCsv;
    }

    // 4. website
    @Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
    public static class Website {
        private Section header;
        private Section footer;
        private Section home;
        private Section auth;
        private Section settings;

        @Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
        public static class Section {
            private List<String> items;
            private String notes;
        }
    }

    // 5. ecom
    @Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
    public static class Ecom {
        private String variants;
        private String taxRegions;
        private String gateways;
        private String shipping;
    }

    // 7. mobile
    @Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
    public static class Mobile {
        private String targets;
        private String approach;
        private String push;
        private String offline;
    }

    // 8. seo
    @Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
    public static class Seo {
        private String pages;
        private String schema;
        private String kpis;
        private String cadence; // weekly | biweekly | monthly
    }

    // 9. content
    @Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
    public static class Content {
        private String types;
        private String tone;
    }

    // 10. marketing
    @Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
    public static class Marketing {
        private String channels;
        private String split;
    }

    // 11. technical
    @Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
    public static class Technical {
        private List<String> preferredStack;
        private String dbChoice;
        private String hosting;       // cloud | onprem | hybrid
        private String frontend;      // Required | Not Required
        private String backend;       // JAVA | PYTHON | .NET | NODE.js
        private String frameworks;    // as per UI
        private String deployModel;   // cloud | onprem | hybrid
        private String releaseStrategy; // continuous | scheduled
        private String supportSla;
    }
    // 12. uiux
    @Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
    public static class UiUx {
        private String brandColors;
        private Boolean hasWireframes;
        private Boolean responsive;
    }
}
