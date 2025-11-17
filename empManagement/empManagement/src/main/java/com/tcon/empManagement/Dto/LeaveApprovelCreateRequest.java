package com.tcon.empManagement.Dto;


import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveApprovelCreateRequest {
    @NotBlank
    private String empId;

    @NotBlank
    private String empName; // should be concatenated as firstName + " " + lastName

    @NotBlank
    private String empRole; // "EMPLOYEE", "MANAGER", "HR", "CEO"

    @NotBlank
    private String typeOfLeave;

    @NotNull
    private LocalDate fromDate;

    @NotNull
    private LocalDate toDate;

    @NotBlank
    private String reason;
}

