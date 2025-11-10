package com.tcon.empManagement.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FieldTableResponse {

    private String id;

    private String taskName;

    private String taskDescription;

    private String priority;

    private String type;

    private String deptName;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
