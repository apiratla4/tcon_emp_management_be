package com.tcon.empManagement.Dto;
import lombok.*;
import java.time.OffsetDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class AttendanceUpdateRequest {
    private OffsetDateTime checkOut;
    private String status;
}

