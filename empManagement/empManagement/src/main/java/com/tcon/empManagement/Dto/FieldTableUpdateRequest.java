package com.tcon.empManagement.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FieldTableUpdateRequest {

    private String taskName;

    private String taskDescription;

    private String priority; // HIGH, MEDIUM, LOW

    private String type;

    private String deptName;
}
