package com.tcon.empManagement.Dto;

import lombok.*;

import java.time.Instant;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
public class EmployeeAttendanceSummaryResponse {
    private String empId;
    private int noOfDaysPresents;
    private int noOfLeavesTaken;
    private int lossOfPayDays;
    private int year;
    private int month;
    private int workingDays;
    private String dept;
    private String panNo;
    private Instant joiningDate;
}
