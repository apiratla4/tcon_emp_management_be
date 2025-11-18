package com.tcon.empManagement.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "story_table")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoryTable {

    @Id
    private String id;

    private String taskName;  // Added from FieldTable

    private String taskDescription;  // Added from FieldTable

    private String type;  // Added from FieldTable

    private String description;

    private String assignedTo = "unassigned";

    private String project;

    private String department;

    private String priority; // HIGH, MEDIUM, LOW

    private String status = "PENDING"; // PENDING, IN_PROGRESS, COMPLETED

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Integer sprintNumber;
    private List<Integer> previousSprints;

    private Boolean spillover = false;
    private Integer spilloverFromSprint;

    private String empId;
    private LocalDateTime dueDate;

}
