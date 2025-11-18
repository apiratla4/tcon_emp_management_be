package com.tcon.empManagement.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientOnBoardUpdateRequest {

    private String projectId;

    private ClientOnBoardCreateRequest.ClientInfoDto clientInfo;

    private ClientOnBoardCreateRequest.ProjectTypeDto projectType;

    private ClientOnBoardCreateRequest.TechnicalDto technical;

    private ClientOnBoardCreateRequest.UiUxDto uiux;

    private List<ClientOnBoardCreateRequest.FileUploadDto> fileUploads;

    private String description;
    private String status;
    private String note;

    private ClientOnBoardCreateRequest.ContactInfoDto contactInfo;
}
