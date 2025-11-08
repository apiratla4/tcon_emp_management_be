package com.tcon.empManagement.Dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeTaskHistoryUpdateRequest {

    @Size(max = 120)
    private String taskName;

    @Size(max = 500)
    private String taskDescription;

    @Pattern(regexp = "ASSIGNED|PENDING|COMPLETED|CANCELLED")
    private String status;

    private Instant dueDate;

    // Allow updates to set/refresh lifecycle timestamps explicitly, else service will set appropriately
    private Instant updatedAtDateTime;
    private Instant completedAtDateTime;
}
