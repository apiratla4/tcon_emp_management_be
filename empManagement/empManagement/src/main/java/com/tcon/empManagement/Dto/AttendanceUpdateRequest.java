package com.tcon.empManagement.Dto;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import java.time.OffsetDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class AttendanceUpdateRequest {
    private OffsetDateTime checkOut;
    @Pattern(regexp = "Present|Absent|Leave")
    private String status;
}

