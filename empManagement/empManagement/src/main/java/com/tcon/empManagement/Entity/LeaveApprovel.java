package com.tcon.empManagement.Entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "leave_approvel")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LeaveApprovel {

    @Id
    private String id;

    private String empId;
    private String empName;

    @Field("emp_role")
    private String empRole; // "EMPLOYEE", "MANAGER", "HR", "CEO"

    private String typeOfLeave;
    private LocalDate fromDate;
    private LocalDate toDate;
    private Double noOfDays; // Calculated as toDate-fromDate+1 (inclusive)
    private String reason;
    // Status: PENDING (default), APPROVED, REJECTED
    private String status;

    private LocalDateTime createDate;       // Request creation timestamp
    private LocalDateTime statusUpdateDate; // Last status change timestamp
}
