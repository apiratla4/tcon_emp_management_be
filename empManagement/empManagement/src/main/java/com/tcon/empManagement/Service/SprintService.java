package com.tcon.empManagement.Service;

import com.tcon.empManagement.Entity.Sprint;

import java.util.List;
import java.util.Map;

public interface SprintService {


    List<Sprint> generateSprintsForProject(String project, int year);

    Sprint createSprint(Sprint sprint);


    List<Sprint> getSprintsForProject(String project);

    Sprint getCurrentSprintForProject(String project);

    Sprint activateSprint(String project, Integer sprintNumber);

    Map<String, Integer> getCurrentSprintNumbersPerProject();

    Integer getGlobalCurrentSprint();

    Sprint getSprintByProjectAndNumber(String project, Integer sprintNumber);

    // NEW
    void deleteSprintById(String id);
    boolean existsById(String id);
}
