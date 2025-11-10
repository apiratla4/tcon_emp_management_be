package com.tcon.empManagement.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



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
}
