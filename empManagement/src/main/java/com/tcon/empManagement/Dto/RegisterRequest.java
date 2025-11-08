package com.tcon.empManagement.Dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
    @NotBlank @Email @Size(max = 120)
    private String email;

    @NotBlank @Size(min=6)
    private String password;

    @NotBlank @Size(max=40)
    private String empRole; // employee, manager, owner

    @NotBlank @Pattern(regexp = "EMP-[A-Z0-9]{4,20}")
    private String empId;

    private boolean isActive = true;
}
