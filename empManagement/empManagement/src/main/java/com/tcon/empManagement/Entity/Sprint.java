package com.tcon.empManagement.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "sprints")
public class Sprint {

    @Id
    private String id;

    private String project;        // project.clientInfo.projectName
    private Integer sprintNumber;  // 1..52
    private Integer year;          // 2025

    private LocalDate startDate;
    private LocalDate endDate;

    private Boolean active; // only 1 sprint per project is active
}
