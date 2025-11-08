package com.tcon.empManagement.Dto;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeTaskHistoryCreateRequest {

    @NotBlank
    private String empId;

    @NotBlank
    @Size(max = 140)
    private String empName; // firstName + lastName from caller

    @NotBlank
    @Size(max = 120)
    private String taskName;

    @NotBlank
    @Size(max = 500)
    private String taskDescription;

    // Optional at create; default ASSIGNED when omitted
    @Pattern(regexp = "ASSIGNED|PENDING|COMPLETED|CANCELLED")
    private String status;

    // Optional due date
    private Instant dueDate;

    @NotBlank
    @Size(max = 120)
    private String taskAssignedBy;

    // Optional explicit timestamps if caller wants to override defaults
    private Instant createdAtDateTime;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
