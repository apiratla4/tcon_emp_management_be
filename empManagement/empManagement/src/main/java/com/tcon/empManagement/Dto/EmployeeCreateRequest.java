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
public class EmployeeCreateRequest {

    @NotBlank
    @Size(max = 40)
    private String title;

    private String firstName;

    private String lastName;

    @NotBlank
    @Email
    @Size(max = 120)
    private String email;

    @NotBlank
    @Size(min = 8, max = 120)
    private String password;

    @NotNull
    @Min(6000000000L)
    @Max(9999999999L)
    private Long phoneNumber;

    @NotBlank
    @Size(max = 40)
    private String empRole;

    @NotBlank
    private String empId;

    @Size(max = 10)
    @Pattern(regexp = "^(A|B|AB|O)[+-]?$")
    private String bloodGroup;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    private Double salary;

    private String status; // ACTIVE or INACTIVE (default: ACTIVE)

    @NotNull
    private AddressDto address;

    @NotNull
    private BankDetailsDto bankDetails;

    @NotNull
    private EmergencyContactDto emergencyContact;

    // Nested DTOs
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class AddressDto {
        @NotBlank
        @Size(max = 120)
        private String address1;

        @Size(max = 120)
        private String address2;

        @NotBlank
        @Size(max = 60)
        private String country;

        @NotBlank
        @Size(max = 60)
        private String city;

        @NotNull
        @Min(100000)
        @Max(999999)
        private Integer pincode;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BankDetailsDto {
        @NotNull
        @Min(1000000000L)
        @Max(999999999999999L)
        private Long bankAccount;

        @NotBlank
        @Pattern(regexp = "^[A-Z]{4}[A-Z0-9]{7}$")
        private String ifscCode;

        @NotBlank
        @Size(max = 80)
        private String bankName;

        @NotBlank
        @Size(max = 80)
        private String branchName;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class EmergencyContactDto {
        @NotBlank
        @Size(max = 80)
        private String name;

        @NotNull
        @Min(6000000000L)
        @Max(9999999999L)
        private Long contactNumber;

        @NotBlank
        @Size(max = 40)
        private String relation;
    }
}
