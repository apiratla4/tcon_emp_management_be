package com.tcon.empManagement.Dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private String id;
    private String empId;
    private String email;
    private String firstName;
    private String lastName;
    private String empRole;
    private String status;
    private String message;
    private boolean success;

    // Custom constructor with 5 parameters
    public LoginResponse(String id, String empId, String email, String empRole, String message) {
        this.id = id;
        this.empId = empId;
        this.email = email;
        this.empRole = empRole;
        this.message = message;
        this.success = true;
    }
}
