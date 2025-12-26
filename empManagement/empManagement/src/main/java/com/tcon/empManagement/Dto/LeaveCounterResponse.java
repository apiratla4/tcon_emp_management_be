package com.tcon.empManagement.Dto;

import lombok.*;
import java.util.List;


@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class LeaveCounterResponse {
    private String empId;
    private int casualLeaves;
    private int sickLeaves;
    private double annualLeaves;
    private List<String> publicHolidays;
    private int lossOfPayLeaves;     // Excess applied leaves deducted as loss of pay

}
