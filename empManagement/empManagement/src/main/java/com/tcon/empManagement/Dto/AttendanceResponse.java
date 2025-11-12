package com.tcon.empManagement.Dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class AttendanceResponse {

    private String id;
    private String empId;
    private String empName;
    private LocalDate date;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private String workMode;
    private String status;
    private Double workHours;
    private String empRole;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
