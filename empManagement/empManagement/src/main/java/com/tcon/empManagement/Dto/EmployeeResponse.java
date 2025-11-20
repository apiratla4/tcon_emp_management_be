package com.tcon.empManagement.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeResponse {

    private String id;

    private String title;

    private String firstName;

    private String lastName;

    private String email;

    private Long phoneNumber;

    private String empRole;

    private String empId;

    private String bloodGroup;

    private Double salary;

    private String status;

    private AddressDto address;

    private BankDetailsDto bankDetails;

    private EmergencyContactDto emergencyContact;

    private Instant createdAt;

    private Instant updatedAt;

    // Nested DTOs
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class AddressDto {
        private String address1;
        private String address2;
        private String country;
        private String city;
        private Integer pincode;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BankDetailsDto {
        private Long bankAccount;
        private String ifscCode;
        private String bankName;
        private String branchName;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class EmergencyContactDto {
        private String name;
        private Long contactNumber;
        private String relation;
    }
    private String profileImageBase64;
    private String profileImageType;

}
