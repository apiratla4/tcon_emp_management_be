package com.tcon.empManagement.Dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    private String role;
    private String empId;
    private String email;

}
