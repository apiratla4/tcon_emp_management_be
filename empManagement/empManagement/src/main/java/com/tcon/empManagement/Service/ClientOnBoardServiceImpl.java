package com.tcon.empManagement.Service;


import com.tcon.empManagement.Dto.ClientOnBoardCreateRequest;
import com.tcon.empManagement.Dto.ClientOnBoardResponse;
import com.tcon.empManagement.Dto.ClientOnBoardUpdateRequest;
import com.tcon.empManagement.Entity.ClientOnBoard;
import com.tcon.empManagement.Repository.ClientOnBoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientOnBoardServiceImpl implements ClientOnBoardService {

    private final ClientOnBoardRepository repo;

    @Override
    public ClientOnBoardResponse create(ClientOnBoardCreateRequest r) {
        log.info("Creating clientOnBoard projectId={}", r.getProjectId());
        try {
            Instant now = Instant.now();
            ClientOnBoard e = mapCreate(r);
            e.setCreatedAt(now);
            e.setUpdatedAt(now);
            ClientOnBoard saved = repo.save(e);
            log.info("ClientOnBoard created id={}", saved.getId());
            return mapResponse(saved);
        } catch (DuplicateKeyException ex) {
            log.error("Duplicate on create projectId={}", r.getProjectId(), ex);
            throw ex;
        } catch (Exception ex) {
            log.error("Create failed projectId={}", r.getProjectId(), ex);
            throw ex;
        }
    }

    @Override
    public ClientOnBoardResponse updateById(String id, ClientOnBoardUpdateRequest r) {
        log.info("Updating clientOnBoard id={}", id);
        try {
            ClientOnBoard e = repo.findById(id).orElseThrow(() -> {
                log.warn("ClientOnBoard not found id={}", id);
                return new NoSuchElementException("ClientOnBoard not found: " + id);
            });

            // partial updates
            if (r.getProjectId() != null) e.setProjectId(r.getProjectId());
            if (r.getClientInfo() != null) e.setClientInfo(mapClientInfo(r.getClientInfo()));
            if (r.getProjectType() != null) e.setProjectType(mapProjectType(r.getProjectType()));
            if (r.getWebsite() != null) e.setWebsite(mapWebsite(r.getWebsite()));
            if (r.getEcom() != null) e.setEcom(mapEcom(r.getEcom()));
            if (r.getMobile() != null) e.setMobile(mapMobile(r.getMobile()));
            if (r.getSeo() != null) e.setSeo(mapSeo(r.getSeo()));
            if (r.getContent() != null) e.setContent(mapContent(r.getContent()));
            if (r.getMarketing() != null) e.setMarketing(mapMarketing(r.getMarketing()));
            if (r.getTechnical() != null) e.setTechnical(mapTechnical(r.getTechnical()));
            if (r.getUiux() != null) e.setUiux(mapUiux(r.getUiux()));

            e.setUpdatedAt(Instant.now());
            ClientOnBoard saved = repo.save(e);
            log.info("ClientOnBoard updated id={}", saved.getId());
            return mapResponse(saved);
        } catch (Exception ex) {
            log.error("Update failed id={}", id, ex);
            throw ex;
        }
    }

    @Override
    public ClientOnBoardResponse getById(String id) {
        log.info("Fetching clientOnBoard id={}", id);
        return repo.findById(id)
                .map(e -> {
                    log.info("Fetched clientOnBoard id={}", id);
                    return mapResponse(e);
                })
                .orElseThrow(() -> {
                    log.warn("ClientOnBoard not found id={}", id);
                    return new NoSuchElementException("ClientOnBoard not found: " + id);
                });
    }

    @Override
    public ClientOnBoardResponse getByProjectId(String projectId) {
        log.info("Fetching clientOnBoard by projectId={}", projectId);
        return repo.findByProjectId(projectId)
                .map(e -> {
                    log.info("Fetched clientOnBoard projectId={}", projectId);
                    return mapResponse(e);
                })
                .orElseThrow(() -> {
                    log.warn("ClientOnBoard not found projectId={}", projectId);
                    return new NoSuchElementException("ClientOnBoard not found for projectId: " + projectId);
                });
    }

    @Override
    public Page<ClientOnBoardResponse> list(Pageable pageable) {
        log.info("Listing clientOnBoard page={} size={}", pageable.getPageNumber(), pageable.getPageSize());
        return repo.findAll(pageable).map(this::mapResponse);
    }

    @Override
    public List<ClientOnBoardResponse> listByProjectId(String projectId) {
        log.info("Listing clientOnBoard by projectId={}", projectId);
        return repo.findAllByProjectId(projectId).stream().map(this::mapResponse).toList();
    }

    @Override
    public void deleteById(String id) {
        log.info("Deleting clientOnBoard id={}", id);
        if (!repo.existsById(id)) {
            log.warn("Delete failed, not found id={}", id);
            throw new NoSuchElementException("ClientOnBoard not found: " + id);
        }
        repo.deleteById(id);
        log.info("Deleted clientOnBoard id={}", id);
    }

    @Override
    public long deleteAllByProjectId(String projectId) {
        log.info("Deleting all clientOnBoard by projectId={}", projectId);
        try {
            long count = repo.deleteByProjectId(projectId);
            log.info("Deleted count={} for projectId={}", count, projectId);
            return count;
        } catch (Exception ex) {
            log.error("Bulk delete by projectId failed projectId={}", projectId, ex);
            throw ex;
        }
    }

    // Mappers
    private ClientOnBoard mapCreate(ClientOnBoardCreateRequest r) {
        return ClientOnBoard.builder()
                .projectId(r.getProjectId())
                .clientInfo(mapClientInfo(r.getClientInfo()))
                .projectType(mapProjectType(r.getProjectType()))
                .website(mapWebsite(r.getWebsite()))
                .ecom(mapEcom(r.getEcom()))
                .mobile(mapMobile(r.getMobile()))
                .seo(mapSeo(r.getSeo()))
                .content(mapContent(r.getContent()))
                .marketing(mapMarketing(r.getMarketing()))
                .technical(mapTechnical(r.getTechnical()))
                .uiux(mapUiux(r.getUiux()))
                .build();
    }

    private ClientOnBoard.ClientInfo mapClientInfo(ClientOnBoardCreateRequest.ClientInfo d) {
        if (d == null) return null;
        return ClientOnBoard.ClientInfo.builder()
                .businessName(d.getBusinessName())
                .stakeholders(d.getStakeholders())
                .projectName(d.getProjectName())
                .budget(d.getBudget())
                .timelineWeeks(d.getTimelineWeeks())
                .build();
    }

    private ClientOnBoard.ProjectType mapProjectType(ClientOnBoardCreateRequest.ProjectType d) {
        if (d == null) return null;
        return ClientOnBoard.ProjectType.builder()
                .features(d.getFeatures())
                .userRoles(d.getUserRoles())
                .integrations(d.getIntegrations())
                .featuresCsv(d.getFeaturesCsv())
                .userRolesCsv(d.getUserRolesCsv())
                .integrationsCsv(d.getIntegrationsCsv())
                .build();
    }

    private ClientOnBoard.Website mapWebsite(ClientOnBoardCreateRequest.Website d) {
        if (d == null) return null;
        return ClientOnBoard.Website.builder()
                .header(mapSection(d.getHeader()))
                .footer(mapSection(d.getFooter()))
                .home(mapSection(d.getHome()))
                .auth(mapSection(d.getAuth()))
                .settings(mapSection(d.getSettings()))
                .build();
    }

    private ClientOnBoard.Website.Section mapSection(ClientOnBoardCreateRequest.Website.Section s) {
        if (s == null) return null;
        return ClientOnBoard.Website.Section.builder()
                .items(s.getItems())
                .notes(s.getNotes())
                .build();
    }

    private ClientOnBoard.Ecom mapEcom(ClientOnBoardCreateRequest.Ecom d) {
        if (d == null) return null;
        return ClientOnBoard.Ecom.builder()
                .variants(d.getVariants())
                .taxRegions(d.getTaxRegions())
                .gateways(d.getGateways())
                .shipping(d.getShipping())
                .build();
    }

    private ClientOnBoard.Mobile mapMobile(ClientOnBoardCreateRequest.Mobile d) {
        if (d == null) return null;
        return ClientOnBoard.Mobile.builder()
                .targets(d.getTargets())
                .approach(d.getApproach())
                .push(d.getPush())
                .offline(d.getOffline())
                .build();
    }

    private ClientOnBoard.Seo mapSeo(ClientOnBoardCreateRequest.Seo d) {
        if (d == null) return null;
        return ClientOnBoard.Seo.builder()
                .pages(d.getPages())
                .schema(d.getSchema())
                .kpis(d.getKpis())
                .cadence(d.getCadence())
                .build();
    }

    private ClientOnBoard.Content mapContent(ClientOnBoardCreateRequest.Content d) {
        if (d == null) return null;
        return ClientOnBoard.Content.builder()
                .types(d.getTypes())
                .tone(d.getTone())
                .build();
    }

    private ClientOnBoard.Marketing mapMarketing(ClientOnBoardCreateRequest.Marketing d) {
        if (d == null) return null;
        return ClientOnBoard.Marketing.builder()
                .channels(d.getChannels())
                .split(d.getSplit())
                .build();
    }

    private ClientOnBoard.Technical mapTechnical(ClientOnBoardCreateRequest.Technical d) {
        if (d == null) return null;
        return ClientOnBoard.Technical.builder()
                .preferredStack(d.getPreferredStack())
                .dbChoice(d.getDbChoice())
                .hosting(d.getHosting())
                .frontend(d.getFrontend())
                .backend(d.getBackend())
                .frameworks(d.getFrameworks())
                .deployModel(d.getDeployModel())
                .releaseStrategy(d.getReleaseStrategy())
                .supportSla(d.getSupportSla())
                .build();
    }

    private ClientOnBoard.UiUx mapUiux(ClientOnBoardCreateRequest.UiUx d) {
        if (d == null) return null;
        return ClientOnBoard.UiUx.builder()
                .brandColors(d.getBrandColors())
                .hasWireframes(d.getHasWireframes())
                .responsive(d.getResponsive())
                .build();
    }

    private ClientOnBoardResponse mapResponse(ClientOnBoard e) {
        return ClientOnBoardResponse.builder()
                .id(e.getId())
                .projectId(e.getProjectId())
                .clientInfo(toDto(e.getClientInfo()))
                .projectType(toDto(e.getProjectType()))
                .website(toDto(e.getWebsite()))
                .ecom(toDto(e.getEcom()))
                .mobile(toDto(e.getMobile()))
                .seo(toDto(e.getSeo()))
                .content(toDto(e.getContent()))
                .marketing(toDto(e.getMarketing()))
                .technical(toDto(e.getTechnical()))
                .uiux(toDto(e.getUiux()))
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .build();
    }

    // entity -> DTO mappers (reuse DTO types)
    private ClientOnBoardCreateRequest.ClientInfo toDto(ClientOnBoard.ClientInfo s) {
        if (s == null) return null;
        return ClientOnBoardCreateRequest.ClientInfo.builder()
                .businessName(s.getBusinessName())
                .stakeholders(s.getStakeholders())
                .projectName(s.getProjectName())
                .budget(s.getBudget())
                .timelineWeeks(s.getTimelineWeeks())
                .build();
    }
    private ClientOnBoardCreateRequest.ProjectType toDto(ClientOnBoard.ProjectType s) {
        if (s == null) return null;
        return ClientOnBoardCreateRequest.ProjectType.builder()
                .features(s.getFeatures())
                .userRoles(s.getUserRoles())
                .integrations(s.getIntegrations())
                .featuresCsv(s.getFeaturesCsv())
                .userRolesCsv(s.getUserRolesCsv())
                .integrationsCsv(s.getIntegrationsCsv())
                .build();
    }
    private ClientOnBoardCreateRequest.Website toDto(ClientOnBoard.Website s) {
        if (s == null) return null;
        return ClientOnBoardCreateRequest.Website.builder()
                .header(toDto(s.getHeader()))
                .footer(toDto(s.getFooter()))
                .home(toDto(s.getHome()))
                .auth(toDto(s.getAuth()))
                .settings(toDto(s.getSettings()))
                .build();
    }
    private ClientOnBoardCreateRequest.Website.Section toDto(ClientOnBoard.Website.Section s) {
        if (s == null) return null;
        return ClientOnBoardCreateRequest.Website.Section.builder()
                .items(s.getItems())
                .notes(s.getNotes())
                .build();
    }
    private ClientOnBoardCreateRequest.Ecom toDto(ClientOnBoard.Ecom s) {
        if (s == null) return null;
        return ClientOnBoardCreateRequest.Ecom.builder()
                .variants(s.getVariants())
                .taxRegions(s.getTaxRegions())
                .gateways(s.getGateways())
                .shipping(s.getShipping())
                .build();
    }
    private ClientOnBoardCreateRequest.Mobile toDto(ClientOnBoard.Mobile s) {
        if (s == null) return null;
        return ClientOnBoardCreateRequest.Mobile.builder()
                .targets(s.getTargets())
                .approach(s.getApproach())
                .push(s.getPush())
                .offline(s.getOffline())
                .build();
    }
    private ClientOnBoardCreateRequest.Seo toDto(ClientOnBoard.Seo s) {
        if (s == null) return null;
        return ClientOnBoardCreateRequest.Seo.builder()
                .pages(s.getPages())
                .schema(s.getSchema())
                .kpis(s.getKpis())
                .cadence(s.getCadence())
                .build();
    }
    private ClientOnBoardCreateRequest.Content toDto(ClientOnBoard.Content s) {
        if (s == null) return null;
        return ClientOnBoardCreateRequest.Content.builder()
                .types(s.getTypes())
                .tone(s.getTone())
                .build();
    }
    private ClientOnBoardCreateRequest.Marketing toDto(ClientOnBoard.Marketing s) {
        if (s == null) return null;
        return ClientOnBoardCreateRequest.Marketing.builder()
                .channels(s.getChannels())
                .split(s.getSplit())
                .build();
    }
    private ClientOnBoardCreateRequest.Technical toDto(ClientOnBoard.Technical s) {
        if (s == null) return null;
        return ClientOnBoardCreateRequest.Technical.builder()
                .preferredStack(s.getPreferredStack())
                .dbChoice(s.getDbChoice())
                .hosting(s.getHosting())
                .frontend(s.getFrontend())
                .backend(s.getBackend())
                .frameworks(s.getFrameworks())
                .deployModel(s.getDeployModel())
                .releaseStrategy(s.getReleaseStrategy())
                .supportSla(s.getSupportSla())
                .build();
    }
    private ClientOnBoardCreateRequest.UiUx toDto(ClientOnBoard.UiUx s) {
        if (s == null) return null;
        return ClientOnBoardCreateRequest.UiUx.builder()
                .brandColors(s.getBrandColors())
                .hasWireframes(s.getHasWireframes())
                .responsive(s.getResponsive())
                .build();
    }
}
