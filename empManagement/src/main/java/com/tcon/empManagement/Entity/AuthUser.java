package com.tcon.empManagement.Entity;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "auth_users")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class AuthUser {
    @Id
    private String id;

    @NotBlank @Email @Indexed(unique = true) @Size(max = 120)
    private String email;

    @NotBlank @Size(min=6, max=255)
    private String password; // BCrypt hash

    @NotBlank @Size(max=40)
    private String empRole;

    @NotBlank @Pattern(regexp = "EMP-[A-Z0-9]{4,20}") @Indexed(unique = true)
    private String empId;

    private boolean isActive = true;
}
