package com.tcon.empManagement.Dto;

import lombok.*;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeTaskHistoryResponse {

    private String id;

    private String empId;
    private String empName;

    private String taskName;
    private String taskDescription;
    private String status;

    private Instant dueDate;

    private Instant createdAtDateTime;
    private Instant updatedAtDateTime;
    private Instant completedAtDateTime;

    private String taskAssignedBy;

    private Instant createdAt;
    private Instant updatedAt;
}
