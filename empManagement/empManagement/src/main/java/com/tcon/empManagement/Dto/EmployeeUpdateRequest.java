package com.tcon.empManagement.Dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeUpdateRequest {

    @Size(max = 40)
    private String title;

    private String firstName;

    private String lastName;

    @Email
    @Size(max = 120)
    private String email;

    @Size(min = 8, max = 120)
    private String password;

    @Min(6000000000L)
    @Max(9999999999L)
    private Long phoneNumber;

    @Size(max = 40)
    private String empRole;

    @Size(max = 10)
    @Pattern(regexp = "^(A|B|AB|O)[+-]?$")
    private String bloodGroup;

    @DecimalMin(value = "0.0", inclusive = true)
    private Double salary;

    private String status; // ACTIVE or INACTIVE

    private AddressDto address;

    private BankDetailsDto bankDetails;

    private EmergencyContactDto emergencyContact;

    // Nested DTOs
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class AddressDto {
        @Size(max = 120)
        private String address1;

        @Size(max = 120)
        private String address2;

        @Size(max = 60)
        private String country;

        @Size(max = 60)
        private String city;

        @Min(100000)
        @Max(999999)
        private Integer pincode;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BankDetailsDto {
        @Min(1000000000L)
        @Max(999999999999999L)
        private Long bankAccount;

        @Pattern(regexp = "^[A-Z]{4}[A-Z0-9]{7}$")
        private String ifscCode;

        @Size(max = 80)
        private String bankName;

        @Size(max = 80)
        private String branchName;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class EmergencyContactDto {
        @Size(max = 80)
        private String name;

        @Min(6000000000L)
        @Max(9999999999L)
        private Long contactNumber;

        @Size(max = 40)
        private String relation;
    }
}
