package com.tcon.empManagement.Entity;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "employees")
@Getter @Setter
@Builder
@AllArgsConstructor @NoArgsConstructor
public class Employee {

    @Id
    private String id;

    @NotBlank @Size(max = 40)
    private String title;

    private String firstName;

    private String lastName;

    @NotBlank @Email @Size(max = 120)
    @Indexed(unique = true)
    private String email;

    @NotBlank @Size(min = 8, max = 120)
    private String password;

    @NotNull @Min(6000000000L) @Max(9999999999L)
    @Indexed
    private Long phoneNumber;

    @NotBlank @Size(max = 40)
    private String empRole;

    @Indexed(unique = true)
    private String empId;

    @Size(max = 10)
    @Pattern(regexp = "^(A|B|AB|O)[+-]?$")
    private String bloodGroup;

    @NotNull @DecimalMin(value = "0.0", inclusive = true)
    private Double salary;

    @NotNull
    private Address address;

    @NotNull
    private BankDetails bankDetails;

    @NotNull
    private EmergencyContact emergencyContact;

    // NEW: Status field
    @NotBlank
    @Builder.Default
    private String status = "ACTIVE"; // ACTIVE or INACTIVE
    @CreatedDate
    private Instant createdAt;


    @LastModifiedDate
    private Instant updatedAt;

    @Getter @Setter
    @AllArgsConstructor @NoArgsConstructor @Builder
    public static class Address {
        @NotBlank @Size(max = 120)
        private String address1;
        @Size(max = 120)
        private String address2;
        @NotBlank @Size(max = 60)
        private String country;
        @NotBlank @Size(max = 60)
        private String city;
        @NotNull @Min(100000) @Max(999999)
        private Integer pincode;
    }

    @Getter @Setter
    @AllArgsConstructor @NoArgsConstructor @Builder
    public static class BankDetails {
        @NotNull @Min(1000000000L) @Max(999999999999999L)
        private Long bankAccount;
        @NotBlank
        @Pattern(regexp = "^[A-Z]{4}[A-Z0-9]{7}$")
        private String ifscCode;
        @NotBlank @Size(max = 80)
        private String bankName;
        @NotBlank @Size(max = 80)
        private String branchName;
    }

    @Getter @Setter
    @AllArgsConstructor @NoArgsConstructor @Builder
    public static class EmergencyContact {
        @NotBlank @Size(max = 80)
        private String name;
        @NotNull @Min(6000000000L) @Max(9999999999L)
        private Long contactNumber;
        @NotBlank @Size(max = 40)
        private String relation;
    }

    public String getFullName() {
        String first = (this.firstName != null && !this.firstName.isEmpty()) ? this.firstName : "";
        String last = (this.lastName != null && !this.lastName.isEmpty()) ? this.lastName : "";

        if (first.isEmpty() && last.isEmpty()) {
            return "Unknown";
        }

        return (first + " " + last).trim();
    }
}
