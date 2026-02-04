package com.tcon.empManagement.Entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "employee_attendance_summary")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
public class EmployeeAttendanceSummary {
    @Id
    private String id;
    private String empId;
    private int noOfDaysPresents;
    private int noOfLeavesTaken;
    private int lossOfPayDays;

    // NEW: month-wise context
    private int year;          // e.g. 2025
    private int month;         // 1 = Jan ... 12 = Dec
    private int workingDays;   // calculated working days in that month
    private String dept;
    private String panNo;
    private Instant joiningDate;
}

