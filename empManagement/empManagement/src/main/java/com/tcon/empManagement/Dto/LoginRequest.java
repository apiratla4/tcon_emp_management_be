package com.tcon.empManagement.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class LoginRequest {
    @NotBlank
    private String empId;

    @NotBlank
    private String password;
}
