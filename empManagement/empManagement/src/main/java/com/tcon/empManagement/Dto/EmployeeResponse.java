package com.tcon.empManagement.Dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeResponse {

    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private Long phoneNumber;
    private String empRole;
    private String empId;
    private String bloodGroup;
    private Double salary;
    private Address address;
    private BankDetails bankDetails;
    private EmergencyContact emergencyContact;
    private Instant createdAt;
    private Instant updatedAt;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Address {
        private String address1;
        private String address2;
        private String country;
        private String city;
        private Integer pincode;

    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BankDetails {
        private Long bankAccount;
        private String ifscCode;
        private String bankName;
        private String branchName;

    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class EmergencyContact {
        private String name;
        private Long contactNumber;
        private String relation;

    }
}
