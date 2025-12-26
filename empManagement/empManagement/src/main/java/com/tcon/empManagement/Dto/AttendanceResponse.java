package com.tcon.empManagement.Dto;
import lombok.*;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class AttendanceResponse {
    private String id;
    private String empId;
    private String empName;
    private LocalDate date;
    private OffsetDateTime checkIn;
    private OffsetDateTime checkOut;
    private String workMode;
    private String status;
    private Double workHours;
    private String empRole;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
