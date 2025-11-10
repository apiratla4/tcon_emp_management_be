package com.tcon.empManagement.Dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeCreateRequest {

    private String firstName;
    private String lastName;
    private String title;
    @NotBlank @Email @Size(max = 120)
    private String email;

    @NotNull @Min(6000000000L) @Max(9999999999L)
    private Long phoneNumber;

    @NotBlank @Size(max = 40)
    private String empRole;

    @NotBlank @Size(min = 4, max = 40)
    @Pattern(regexp = "EMP-[A-Z0-9]{4,20}")
    private String empId;

    @Size(max = 10)
    @Pattern(regexp = "^(A|B|AB|O)[+-]?$")
    private String bloodGroup;

    @NotNull @DecimalMin(value = "0.0", inclusive = true)
    private Double salary;

    @NotNull
    private AddressDto address;

    @NotNull
    private BankDetailsDto bankDetails;

    @NotNull
    private EmergencyContactDto emergencyContact;

    private String password;
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AddressDto {
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
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BankDetailsDto {
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
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class EmergencyContactDto {
        @NotBlank @Size(max = 80)
        private String name;

        @NotNull @Min(6000000000L) @Max(9999999999L)
        private Long contactNumber;

        @NotBlank @Size(max = 40)
        private String relation;
    }
}
