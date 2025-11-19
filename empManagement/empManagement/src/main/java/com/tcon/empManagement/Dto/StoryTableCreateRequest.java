package com.tcon.empManagement.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoryTableCreateRequest {


    private String taskName;

    private String taskDescription;

    private String type;

    private String description;

    private String assignedTo = "unassigned";

    private String project;

    private String department;

    private String priority;

    private String status = "PENDING";
    private Integer sprintNumber;
    private String empId;
    private LocalDateTime dueDate;

    private String acceptanceCriteria;
}
