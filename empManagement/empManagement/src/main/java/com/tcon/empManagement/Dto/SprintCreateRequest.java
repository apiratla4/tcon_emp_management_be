package com.tcon.empManagement.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SprintCreateRequest {
    private String project;
    private Integer sprintNumber;
    private Integer year;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean active;
}
