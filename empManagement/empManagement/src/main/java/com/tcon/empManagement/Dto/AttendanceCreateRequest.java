package com.tcon.empManagement.Dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class AttendanceCreateRequest {
    @NotBlank private String empId;
    @NotBlank private String empName;
    @NotNull private LocalDate date;
    @NotNull private OffsetDateTime checkIn;
    @NotBlank @Pattern(regexp = "WFH|Office") private String workMode;
    @NotBlank @Pattern(regexp = "Present|Absent|Leave") private String status;
    @NotBlank private String empRole;
}