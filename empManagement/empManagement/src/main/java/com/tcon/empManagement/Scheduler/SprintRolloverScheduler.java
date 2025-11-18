package com.tcon.empManagement.Scheduler;

import com.tcon.empManagement.Entity.StoryTable;
import com.tcon.empManagement.Repository.StoryTableRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;

import java.time.LocalDateTime;
import java.util.*;

/**
 * SprintRolloverScheduler
 *
 * - If a SprintService bean is available and returns project-scoped sprint numbers,
 *   it will perform rollover per project (recommended if sprints are project based).
 * - Otherwise it falls back to a configured global sprint number (app.default-current-sprint).
 *
 * Cron/zone currently set to run weekly Monday 00:00 Asia/Kolkata; change as required.
 */
@Component
public class SprintRolloverScheduler {

    private static final Logger log = LoggerFactory.getLogger(SprintRolloverScheduler.class);

    private final StoryTableRepository storyTableRepository;
    private final SprintService sprintService; // may be null if not provided
    private final int defaultSprintFallback;

    public SprintRolloverScheduler(StoryTableRepository storyTableRepository,
                                   @Nullable @Lazy SprintService sprintService,
                                   @Value("${app.default-current-sprint:1}") int defaultSprintFallback) {
        this.storyTableRepository = storyTableRepository;
        this.sprintService = sprintService;
        this.defaultSprintFallback = defaultSprintFallback;
    }

    /**
     * Runs weekly at 00:00 on Monday (Asia/Kolkata timezone).
     * Adjust cron expression & zone to match your sprint schedule.
     */
    @Scheduled(cron = "0 0 0 ? * MON", zone = "Asia/Kolkata")
    @Transactional
    public void processSprintRollOver() {
        try {
            if (sprintService != null) {
                performProjectScopedRollover();
            } else {
                performGlobalRollover();
            }
        } catch (Exception ex) {
            log.error("Error during sprint rollover", ex);
        }
    }

    /**
     * Global fallback: use single sprint number (configured) and move unfinished stories
     * from sprint -> sprint+1.
     */
    private void performGlobalRollover() {
        int currentSprint = determineGlobalSprint();
        log.info("Global sprint rollover started for sprint {}", currentSprint);

        List<StoryTable> stories = storyTableRepository.findBySprintNumber(currentSprint);
        if (stories == null || stories.isEmpty()) {
            log.info("No stories found for sprint {}. Nothing to rollover.", currentSprint);
            return;
        }

        int rolled = 0;
        for (StoryTable s : stories) {
            String status = s.getStatus() == null ? "" : s.getStatus().trim();
            if ("COMPLETED".equalsIgnoreCase(status) || "CANCELLED".equalsIgnoreCase(status)) {
                continue;
            }
            applySpillover(s, currentSprint);
            storyTableRepository.save(s);
            rolled++;
            log.debug("Rolled over story id={} to sprint {}", s.getId(), currentSprint + 1);
        }
        log.info("Global sprint rollover finished. {} stories rolled over from {} to {}.", rolled, currentSprint, currentSprint + 1);
    }

    /**
     * Project-scoped: ask SprintService for per-project current sprint numbers and process each project separately.
     */
    private void performProjectScopedRollover() {
        Map<String, Integer> projectSprintMap = sprintService.getCurrentSprintNumbersPerProject();
        if (projectSprintMap == null || projectSprintMap.isEmpty()) {
            log.warn("SprintService returned no project sprint numbers; falling back to global mode using {}", defaultSprintFallback);
            performGlobalRollover();
            return;
        }

        int totalRolled = 0;
        for (Map.Entry<String, Integer> entry : projectSprintMap.entrySet()) {
            String projectName = entry.getKey();
            Integer currentSprint = entry.getValue();
            if (currentSprint == null) continue;

            log.info("Rollover for project '{}' sprint {}", projectName, currentSprint);
            // If you add repository method findByProjectAndSprintNumber, replace below with that call.
            // For now we filter by findBySprintNumber and project because repository has findByProject(...)
            List<StoryTable> candidates = storyTableRepository.findBySprintNumber(currentSprint);
            if (candidates == null || candidates.isEmpty()) {
                log.info("No stories for project '{}' sprint {}", projectName, currentSprint);
                continue;
            }

            int rolledForProject = 0;
            for (StoryTable s : candidates) {
                if (!projectName.equals(s.getProject())) continue; // restrict to this project
                String status = s.getStatus() == null ? "" : s.getStatus().trim();
                if ("COMPLETED".equalsIgnoreCase(status) || "CANCELLED".equalsIgnoreCase(status)) continue;
                applySpillover(s, currentSprint);
                // set to next sprint relative to that project's sprint numbering
                s.setSprintNumber(currentSprint + 1);
                storyTableRepository.save(s);
                rolledForProject++;
            }
            log.info("Project '{}' rollover: {} stories rolled from {} to {}.", projectName, rolledForProject, currentSprint, currentSprint + 1);
            totalRolled += rolledForProject;
        }
        log.info("Project-scoped sprint rollover finished. Total rolled: {}", totalRolled);
    }

    private void applySpillover(StoryTable s, int currentSprint) {
        if (s.getPreviousSprints() == null) {
            s.setPreviousSprints(new ArrayList<>());
        }
        if (!s.getPreviousSprints().contains(currentSprint)) {
            s.getPreviousSprints().add(currentSprint);
        }
        s.setSpillover(true);
        s.setSpilloverFromSprint(currentSprint);
        s.setSprintNumber(currentSprint + 1);
        s.setUpdatedAt(LocalDateTime.now());
    }

    private int determineGlobalSprint() {
        try {
            if (sprintService != null) {
                Integer gs = sprintService.getGlobalCurrentSprint();
                if (gs != null && gs > 0) {
                    return gs;
                }
                log.warn("SprintService returned null/invalid global sprint number; using fallback {}", defaultSprintFallback);
            }
        } catch (Exception ex) {
            log.warn("Exception while getting global sprint from SprintService: {}", ex.getMessage());
        }
        return defaultSprintFallback;
    }
}
