package com.tcon.empManagement.Dto;

import lombok.*;

import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class AttendanceUpdateRequest {

    private LocalDateTime checkOut;
    private String status;              // Can update status on check-out if needed
}
