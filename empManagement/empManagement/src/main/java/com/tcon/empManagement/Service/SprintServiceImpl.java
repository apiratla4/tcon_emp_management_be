package com.tcon.empManagement.Service;

import com.tcon.empManagement.Entity.Sprint;
import com.tcon.empManagement.Repository.SprintRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
public class SprintServiceImpl implements SprintService {

    private final SprintRepository sprintRepository;

    public SprintServiceImpl(SprintRepository sprintRepository) {
        this.sprintRepository = sprintRepository;
    }

    @Override
    public List<Sprint> generateSprintsForProject(String project, int year) {
        LocalDate start = LocalDate.of(year, 1, 1);

        // Move to next Monday (1 = Monday)
        while (start.getDayOfWeek().getValue() != 1) {
            start = start.plusDays(1);
        }

        List<Sprint> list = new ArrayList<>();

        for (int i = 1; i <= 52; i++) {
            LocalDate s = start.plusWeeks(i - 1);
            LocalDate e = s.plusDays(6);

            Sprint sprint = new Sprint();
            sprint.setProject(project);
            sprint.setSprintNumber(i);
            sprint.setYear(year);
            sprint.setStartDate(s);
            sprint.setEndDate(e);
            sprint.setActive(i == 1); // mark sprint 1 active by default

            list.add(sprint);
        }

        return sprintRepository.saveAll(list);
    }

    @Override
    public Sprint createSprint(Sprint sprint) {
        // If creating an active sprint, deactivate others of same project first
        if (Boolean.TRUE.equals(sprint.getActive()) && sprint.getProject() != null) {
            Sprint existingActive = sprintRepository.findByProjectAndActive(sprint.getProject(), true);
            if (existingActive != null) {
                existingActive.setActive(false);
                sprintRepository.save(existingActive);
            }
        }
        return sprintRepository.save(sprint);
    }

    @Override
    public List<Sprint> getSprintsForProject(String project) {
        return sprintRepository.findByProjectOrderBySprintNumberAsc(project);
    }

    @Override
    public Sprint getCurrentSprintForProject(String project) {
        return sprintRepository.findByProjectAndActive(project, true);
    }

    @Override
    @Transactional
    public Sprint activateSprint(String project, Integer sprintNumber) {
        // Deactivate currently active
        Sprint current = sprintRepository.findByProjectAndActive(project, true);
        if (current != null && !Objects.equals(current.getSprintNumber(), sprintNumber)) {
            current.setActive(false);
            sprintRepository.save(current);
        }

        Sprint target = sprintRepository.findByProjectAndSprintNumber(project, sprintNumber);
        if (target == null) {
            // optionally create if missing
            target = new Sprint();
            target.setProject(project);
            target.setSprintNumber(sprintNumber);
            target.setYear(LocalDate.now().getYear());
            // compute simple start/end (naive): find week by sprintNumber relative to year start monday
            LocalDate start = LocalDate.of(target.getYear(), 1, 1);
            while (start.getDayOfWeek().getValue() != 1)
                start = start.plusDays(1);
            LocalDate s = start.plusWeeks(sprintNumber - 1);
            target.setStartDate(s);
            target.setEndDate(s.plusDays(6));
        }
        target.setActive(true);
        return sprintRepository.save(target);
    }

    @Override
    public Map<String, Integer> getCurrentSprintNumbersPerProject() {
        Map<String, Integer> map = new HashMap<>();
        List<Sprint> all = sprintRepository.findAll();
        Set<String> projects = new HashSet<>();
        all.forEach(s -> {
            if (s.getProject() != null) projects.add(s.getProject());
        });

        for (String project : projects) {
            Sprint active = sprintRepository.findByProjectAndActive(project, true);
            if (active != null) {
                map.put(project, active.getSprintNumber());
            }
        }
        return map;
    }

    @Override
    public Integer getGlobalCurrentSprint() {
        Sprint s = sprintRepository.findAll().stream()
                .filter(sp -> Boolean.TRUE.equals(sp.getActive()))
                .findFirst()
                .orElse(null);
        return (s != null) ? s.getSprintNumber() : 1;
    }

    @Override
    public void deleteSprintById(String id) {
        if (id == null) throw new IllegalArgumentException("id cannot be null");
        if (!sprintRepository.existsById(id)) {
            throw new NoSuchElementException("Sprint not found with id: " + id);
        }
        sprintRepository.deleteById(id);
    }

    @Override
    public boolean existsById(String id) {
        return id != null && sprintRepository.existsById(id);
    }

    @Override
    public Sprint getSprintByProjectAndNumber(String project, Integer sprintNumber) {
        if (project == null || sprintNumber == null) return null;
        return sprintRepository.findByProjectAndSprintNumber(project, sprintNumber);
    }


}
