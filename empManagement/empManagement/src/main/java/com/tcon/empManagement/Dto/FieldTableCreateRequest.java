package com.tcon.empManagement.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FieldTableCreateRequest {

    @NotBlank(message = "Task name is required")
    private String taskName;

    @NotBlank(message = "Task description is required")
    private String taskDescription;

    @NotBlank(message = "Priority is required")
    private String priority; // HIGH, MEDIUM, LOW

    @NotBlank(message = "Type is required")
    private String type;

    @NotBlank(message = "Department name is required")
    private String deptName;
}
