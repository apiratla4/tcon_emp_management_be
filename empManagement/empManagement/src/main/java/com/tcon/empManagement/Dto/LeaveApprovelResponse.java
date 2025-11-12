package com.tcon.empManagement.Dto;


import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class LeaveApprovelResponse {
    private String id;
    private String empId;
    private String empName;
    private String typeOfLeave;
    private LocalDate fromDate;
    private LocalDate toDate;
    private Double noOfDays;
    private String reason;
    private String status;
    private LocalDateTime createDate;
    private LocalDateTime statusUpdateDate;
}
