package com.tcon.empManagement.Entity;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "employee_task_history")
@Getter @Setter
@Builder
@AllArgsConstructor @NoArgsConstructor
public class EmployeeTaskHistory {

    @Id
    private String id;

    @NotBlank
    @Indexed
    private String empId;          // copied from Employee.empId

    @NotBlank
    @Size(max = 140)
    private String empName;        // firstName + lastName captured at assignment time

    @NotBlank
    @Size(max = 120)
    private String taskName;

    @NotBlank
    @Size(max = 500)
    private String taskDescription;

    @NotBlank
    @Pattern(regexp = "ASSIGNED|PENDING|COMPLETED|CANCELLED", message = "Invalid status")
    private String status;         // default ASSIGNED

    // Due date for completion
    private Instant dueDate;

    // Lifecycle timestamps (strings requested, but stored as Instant for consistency)
    private Instant createdAtDateTime;     // assignment time
    private Instant updatedAtDateTime;     // pending/update time
    private Instant completedAtDateTime;   // success completion time

    @NotBlank
    @Size(max = 120)
    private String taskAssignedBy;

    // Auditing
    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
