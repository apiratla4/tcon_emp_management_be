package com.tcon.empManagement.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoryTableResponse {

    private String id;

    private String taskName;

    private String taskDescription;

    private String type;

    private String description;

    private String assignedTo;

    private String project;

    private String department;

    private String priority;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
