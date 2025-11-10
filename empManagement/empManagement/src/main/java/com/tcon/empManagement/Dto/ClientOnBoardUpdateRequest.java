package com.tcon.empManagement.Dto;

import lombok.*;
import java.time.Instant;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ClientOnBoardUpdateRequest {
    private String projectId;
    private ClientOnBoardCreateRequest.ClientInfo clientInfo;
    private ClientOnBoardCreateRequest.ProjectType projectType;
    private ClientOnBoardCreateRequest.Website website;
    private ClientOnBoardCreateRequest.Ecom ecom;
    private ClientOnBoardCreateRequest.Mobile mobile;
    private ClientOnBoardCreateRequest.Seo seo;
    private ClientOnBoardCreateRequest.Content content;
    private ClientOnBoardCreateRequest.Marketing marketing;
    private ClientOnBoardCreateRequest.Technical technical;
    private ClientOnBoardCreateRequest.UiUx uiux;
}
