package com.tcon.empManagement.Entity;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "employee_history")
@Getter @Setter
@Builder
@AllArgsConstructor @NoArgsConstructor
public class EmployeeHistory {

    @Id
    private String historyId;

    // Reference to original employee
    private String employeeId;

    // Operation tracking
    private String operation; // CREATE, UPDATE, DELETE

    private String changedBy; // User who made the change

    private Instant operationTime;

    // Specific timestamps for each operation type
    private Instant createdTime;

    private Instant updatedTime;

    private Instant deletedTime;

    // === All Employee fields (snapshot) ===

    private String id;

    private String title;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private Long phoneNumber;

    private String empRole;

    private String empId;

    private String bloodGroup;

    private Double salary;

    private String status; // ACTIVE or INACTIVE

    private Address address;

    private BankDetails bankDetails;

    private EmergencyContact emergencyContact;

    private Instant createdAt;

    private Instant updatedAt;

    // Nested classes - same as Employee
    @Getter @Setter
    @AllArgsConstructor @NoArgsConstructor @Builder
    public static class Address {
        private String address1;
        private String address2;
        private String country;
        private String city;
        private Integer pincode;
    }

    @Getter @Setter
    @AllArgsConstructor @NoArgsConstructor @Builder
    public static class BankDetails {
        private Long bankAccount;
        private String ifscCode;
        private String bankName;
        private String branchName;
    }

    @Getter @Setter
    @AllArgsConstructor @NoArgsConstructor @Builder
    public static class EmergencyContact {
        private String name;
        private Long contactNumber;
        private String relation;
    }
}
