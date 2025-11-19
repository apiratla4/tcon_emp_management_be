package com.tcon.empManagement.Repository;

import com.tcon.empManagement.Entity.Sprint;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SprintRepository extends MongoRepository<Sprint, String> {

    List<Sprint> findByProjectOrderBySprintNumberAsc(String project);

    Sprint findByProjectAndSprintNumber(String project, Integer sprintNumber);

    Sprint findByProjectAndActive(String project, Boolean active);
}
