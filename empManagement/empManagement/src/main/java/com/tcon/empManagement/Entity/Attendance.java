package com.tcon.empManagement.Entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "attendance")
@CompoundIndex(name = "unique_emp_date", def = "{'empId': 1, 'date': 1}", unique = true)
@Getter @Setter
@Builder
@AllArgsConstructor @NoArgsConstructor
public class Attendance {

    @Id
    private String id;

    private String empId;          // Employee business ID
    private String empName;
    private LocalDate date;        // yyyy-MM-dd (unique per employee per day)
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private String workMode;       // WFH | Office
    private String status;         // Present | Absent | Leave
    private Double workHours;      // auto-calculated on check-out
    private String empRole;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
