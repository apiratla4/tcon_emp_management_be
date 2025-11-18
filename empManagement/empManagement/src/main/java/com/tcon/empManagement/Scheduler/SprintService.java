package com.tcon.empManagement.Scheduler;


import java.util.Map;

/**
 * SprintService
 *
 * Optional service to provide current sprint numbers per project.
 * Implement this if you want project-scoped rollovers.
 *
 * Example return: Map.of("PROJECT-A", 10, "PROJECT-B", 4)
 */
public interface SprintService {
    /**
     * Return a map of projectName -> currentSprintNumber.
     * If you prefer a global current sprint number, you can return a single
     * entry or provide an alternative method (getGlobalCurrentSprint()) in your implementation.
     */
    Map<String, Integer> getCurrentSprintNumbersPerProject();

    /**
     * Optional convenience method if you maintain a single global sprint number.
     */
    default Integer getGlobalCurrentSprint() {
        Map<String,Integer> map = getCurrentSprintNumbersPerProject();
        if (map == null || map.isEmpty()) return null;
        // return any value (not ideal for project-scoped systems) — override if needed
        return map.values().stream().findFirst().orElse(null);
    }
}
