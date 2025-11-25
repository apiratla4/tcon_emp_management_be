package com.tcon.empManagement.Dto;

import lombok.*;
import java.util.List;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class LeaveCounterResponse {
    private String empId;
    private int casualLeaves;
    private int sickLeaves;
    private int annualLeaves;
    private List<String> publicHolidays;
}
