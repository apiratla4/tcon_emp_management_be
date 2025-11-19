package com.tcon.empManagement.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoryTableUpdateRequest {

    private String taskName;

    private String taskDescription;

    private String type;

    private String description;

    private String assignedTo;

    private String project;

    private String department;

    private String priority;

    private String status;
    private Integer sprintNumber;
    private List<Integer> previousSprints;
    private Boolean spillover;
    private Integer spilloverFromSprint;
    private String empId;
    private LocalDateTime dueDate;
    private String acceptanceCriteria;

}
