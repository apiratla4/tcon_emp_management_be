package com.tcon.empManagement.Dto;


import lombok.*;
import java.util.List;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class LeaveCounterUpdateRequest {
    private int casualLeaves;
    private int sickLeaves;
    private double annualLeaves;
    private int lossOfPayLeaves;     // Excess applied leaves deducted as loss of pay

}