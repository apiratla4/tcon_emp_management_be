package com.tcon.empManagement.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class LeaveApprovelUpdateStatusRequest {
    @NotBlank
    private String status; // "PENDING", "APPROVED", "REJECTED"
}
