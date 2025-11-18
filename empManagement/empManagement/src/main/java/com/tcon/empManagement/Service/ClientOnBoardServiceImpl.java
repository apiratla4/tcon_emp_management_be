package com.tcon.empManagement.Service;

import com.tcon.empManagement.Dto.*;
import com.tcon.empManagement.Entity.ClientOnBoard;
import com.tcon.empManagement.Repository.ClientOnBoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientOnBoardServiceImpl implements ClientOnBoardService {

    @Autowired
    private ClientOnBoardRepository clientOnBoardRepository;

    @Override
    public ClientOnBoardResponse createClientOnBoard(ClientOnBoardCreateRequest request) {
        ClientOnBoard clientOnBoard = ClientOnBoard.builder()
                .projectId(request.getProjectId())
                .clientInfo(mapClientInfo(request.getClientInfo()))
                .projectType(mapProjectType(request.getProjectType()))
                .technical(mapTechnical(request.getTechnical()))
                .uiux(mapUiUx(request.getUiux()))
                .fileUploads(mapFileUploads(request.getFileUploads()))
                .description(request.getDescription())
                .note(request.getNote())
                .contactInfo(mapContactInfo(request.getContactInfo()))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .description(request.getDescription())
                .note(request.getNote())
                .status(request.getStatus())
                .build();

        ClientOnBoard saved = clientOnBoardRepository.save(clientOnBoard);
        return convertToResponse(saved);
    }

    @Override
    public ClientOnBoardResponse updateClientOnBoard(String id, ClientOnBoardUpdateRequest request) {
        ClientOnBoard clientOnBoard = clientOnBoardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ClientOnBoard not found with id: " + id));

        if (request.getStatus() != null) {
            clientOnBoard.setStatus(request.getStatus());
        }

        if (request.getProjectId() != null) {
            clientOnBoard.setProjectId(request.getProjectId());
        }
        if (request.getClientInfo() != null) {
            clientOnBoard.setClientInfo(mapClientInfo(request.getClientInfo()));
        }
        if (request.getProjectType() != null) {
            clientOnBoard.setProjectType(mapProjectType(request.getProjectType()));
        }
        if (request.getTechnical() != null) {
            clientOnBoard.setTechnical(mapTechnical(request.getTechnical()));
        }
        if (request.getUiux() != null) {
            clientOnBoard.setUiux(mapUiUx(request.getUiux()));
        }
        if (request.getFileUploads() != null) {
            clientOnBoard.setFileUploads(mapFileUploads(request.getFileUploads()));
        }
        if (request.getDescription() != null) {
            clientOnBoard.setDescription(request.getDescription());
        }
        if (request.getNote() != null) {
            clientOnBoard.setNote(request.getNote());
        }
        if (request.getContactInfo() != null) {
            clientOnBoard.setContactInfo(mapContactInfo(request.getContactInfo()));
        }

        clientOnBoard.setUpdatedAt(LocalDateTime.now());

        ClientOnBoard updated = clientOnBoardRepository.save(clientOnBoard);
        return convertToResponse(updated);
    }

    @Override
    public ClientOnBoardResponse getClientOnBoardById(String id) {
        ClientOnBoard clientOnBoard = clientOnBoardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ClientOnBoard not found with id: " + id));
        return convertToResponse(clientOnBoard);
    }

    @Override
    public ClientOnBoardResponse getClientOnBoardByProjectId(String projectId) {
        ClientOnBoard clientOnBoard = clientOnBoardRepository.findByProjectId(projectId)
                .orElseThrow(() -> new RuntimeException("ClientOnBoard not found with projectId: " + projectId));
        return convertToResponse(clientOnBoard);
    }

    @Override
    public List<ClientOnBoardResponse> getAllClientOnBoards() {
        return clientOnBoardRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ClientOnBoardResponse> getClientOnBoardsByBusinessName(String businessName) {
        return clientOnBoardRepository.findByClientInfo_BusinessName(businessName).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ClientOnBoardResponse> getClientOnBoardsByContactEmail(String contactEmail) {
        return clientOnBoardRepository.findByContactInfo_ContactEmail(contactEmail).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteClientOnBoard(String id) {
        if (!clientOnBoardRepository.existsById(id)) {
            throw new RuntimeException("ClientOnBoard not found with id: " + id);
        }
        clientOnBoardRepository.deleteById(id);
    }

    // Helper mapping methods
    private ClientOnBoard.ClientInfo mapClientInfo(ClientOnBoardCreateRequest.ClientInfoDto dto) {
        if (dto == null) return null;
        return ClientOnBoard.ClientInfo.builder()
                .businessName(dto.getBusinessName())
                .projectName(dto.getProjectName())
                .clientAddress(dto.getClientAddress())
                .businessPhoneNo(dto.getBusinessPhoneNo())
                .build();
    }

    private ClientOnBoard.ProjectType mapProjectType(ClientOnBoardCreateRequest.ProjectTypeDto dto) {
        if (dto == null) return null;
        return ClientOnBoard.ProjectType.builder()
                .websiteDev(dto.getWebsiteDev())
                .ecommerceApp(dto.getEcommerceApp())
                .mobileApp(dto.getMobileApp())
                .seoServices(dto.getSeoServices())
                .contentManagement(dto.getContentManagement())
                .digitalMarketing(dto.getDigitalMarketing())
                .build();
    }

    private ClientOnBoard.Technical mapTechnical(ClientOnBoardCreateRequest.TechnicalDto dto) {
        if (dto == null) return null;
        return ClientOnBoard.Technical.builder()
                .preferredStack(dto.getPreferredStack())
                .dbChoice(dto.getDbChoice())
                .hosting(dto.getHosting())
                .frontend(dto.getFrontend())
                .backend(dto.getBackend())
                .frameworks(dto.getFrameworks())
                .deployModel(dto.getDeployModel())
                .releaseStrategy(dto.getReleaseStrategy())
                .supportSla(dto.getSupportSla())
                .build();
    }

    private ClientOnBoard.UiUx mapUiUx(ClientOnBoardCreateRequest.UiUxDto dto) {
        if (dto == null) return null;
        return ClientOnBoard.UiUx.builder()
                .brandColors(dto.getBrandColors())
                .hasWireframes(dto.getHasWireframes())
                .responsive(dto.getResponsive())
                .build();
    }

    private List<ClientOnBoard.FileUpload> mapFileUploads(List<ClientOnBoardCreateRequest.FileUploadDto> dtos) {
        if (dtos == null) return null;
        return dtos.stream()
                .map(dto -> ClientOnBoard.FileUpload.builder()
                        .fileName(dto.getFileName())
                        .fileType(dto.getFileType())
                        .fileUrl(dto.getFileUrl())
                        .fileSize(dto.getFileSize())
                        .uploadedAt(LocalDateTime.now())
                        .build())
                .collect(Collectors.toList());
    }

    private ClientOnBoard.ContactInfo mapContactInfo(ClientOnBoardCreateRequest.ContactInfoDto dto) {
        if (dto == null) return null;
        return ClientOnBoard.ContactInfo.builder()
                .contactName(dto.getContactName())
                .contactNumber(dto.getContactNumber())
                .contactEmail(dto.getContactEmail())
                .address(dto.getAddress())
                .build();
    }

    private ClientOnBoardResponse convertToResponse(ClientOnBoard entity) {
        return ClientOnBoardResponse.builder()
                .id(entity.getId())
                .projectId(entity.getProjectId())
                .clientInfo(convertClientInfoToDto(entity.getClientInfo()))
                .projectType(convertProjectTypeToDto(entity.getProjectType()))
                .technical(convertTechnicalToDto(entity.getTechnical()))
                .uiux(convertUiUxToDto(entity.getUiux()))
                .fileUploads(convertFileUploadsToDto(entity.getFileUploads()))
                .description(entity.getDescription())
                .note(entity.getNote())
                .contactInfo(convertContactInfoToDto(entity.getContactInfo()))
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .status(entity.getStatus())
                .build();
    }

    private ClientOnBoardCreateRequest.ClientInfoDto convertClientInfoToDto(ClientOnBoard.ClientInfo info) {
        if (info == null) return null;
        return ClientOnBoardCreateRequest.ClientInfoDto.builder()
                .businessName(info.getBusinessName())
                .projectName(info.getProjectName())
                .clientAddress(info.getClientAddress())
                .businessPhoneNo(info.getBusinessPhoneNo())
                .build();
    }

    private ClientOnBoardCreateRequest.ProjectTypeDto convertProjectTypeToDto(ClientOnBoard.ProjectType type) {
        if (type == null) return null;
        return ClientOnBoardCreateRequest.ProjectTypeDto.builder()
                .websiteDev(type.getWebsiteDev())
                .ecommerceApp(type.getEcommerceApp())
                .mobileApp(type.getMobileApp())
                .seoServices(type.getSeoServices())
                .contentManagement(type.getContentManagement())
                .digitalMarketing(type.getDigitalMarketing())
                .build();
    }

    private ClientOnBoardCreateRequest.TechnicalDto convertTechnicalToDto(ClientOnBoard.Technical tech) {
        if (tech == null) return null;
        return ClientOnBoardCreateRequest.TechnicalDto.builder()
                .preferredStack(tech.getPreferredStack())
                .dbChoice(tech.getDbChoice())
                .hosting(tech.getHosting())
                .frontend(tech.getFrontend())
                .backend(tech.getBackend())
                .frameworks(tech.getFrameworks())
                .deployModel(tech.getDeployModel())
                .releaseStrategy(tech.getReleaseStrategy())
                .supportSla(tech.getSupportSla())
                .build();
    }

    private ClientOnBoardCreateRequest.UiUxDto convertUiUxToDto(ClientOnBoard.UiUx uiux) {
        if (uiux == null) return null;
        return ClientOnBoardCreateRequest.UiUxDto.builder()
                .brandColors(uiux.getBrandColors())
                .hasWireframes(uiux.getHasWireframes())
                .responsive(uiux.getResponsive())
                .build();
    }

    private List<ClientOnBoardResponse.FileUploadResponseDto> convertFileUploadsToDto(List<ClientOnBoard.FileUpload> uploads) {
        if (uploads == null) return null;
        return uploads.stream()
                .map(upload -> ClientOnBoardResponse.FileUploadResponseDto.builder()
                        .fileName(upload.getFileName())
                        .fileType(upload.getFileType())
                        .fileUrl(upload.getFileUrl())
                        .fileSize(upload.getFileSize())
                        .uploadedAt(upload.getUploadedAt())
                        .build())
                .collect(Collectors.toList());
    }

    private ClientOnBoardCreateRequest.ContactInfoDto convertContactInfoToDto(ClientOnBoard.ContactInfo info) {
        if (info == null) return null;
        return ClientOnBoardCreateRequest.ContactInfoDto.builder()
                .contactName(info.getContactName())
                .contactNumber(info.getContactNumber())
                .contactEmail(info.getContactEmail())
                .address(info.getAddress())
                .build();
    }
}
