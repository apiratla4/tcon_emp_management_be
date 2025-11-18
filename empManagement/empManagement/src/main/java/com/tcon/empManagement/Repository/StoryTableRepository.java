package com.tcon.empManagement.Repository;

import com.tcon.empManagement.Entity.StoryTable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoryTableRepository extends MongoRepository<StoryTable, String> {

    List<StoryTable> findByAssignedTo(String assignedTo);

    List<StoryTable> findByDepartment(String department);

    List<StoryTable> findByPriority(String priority);

    List<StoryTable> findByStatus(String status);

    List<StoryTable> findByProject(String project);

    List<StoryTable> findByTaskName(String taskName);

    List<StoryTable> findByType(String type);
    // ...
    List<StoryTable> findBySprintNumber(Integer sprintNumber);
    List<StoryTable> findBySpilloverFromSprint(Integer spilloverFromSprint);
    List<StoryTable> findByEmpId(String empId);
    List<StoryTable> findByProjectAndSprintNumber(String project, Integer sprintNumber);

}
