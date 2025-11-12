package com.tcon.empManagement.Dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class AttendanceCreateRequest {

    @NotBlank
    private String empId;

    @NotBlank
    private String empName;

    @NotNull
    private LocalDate date;             // yyyy-MM-dd

    @NotNull
    private LocalDateTime checkIn;

    @NotBlank
    @Pattern(regexp = "WFH|Office")
    private String workMode;            // WFH | Office

    @NotBlank
    @Pattern(regexp = "Present|Absent|Leave")
    private String status;              // Present | Absent | Leave

    @NotBlank
    private String empRole;
}
