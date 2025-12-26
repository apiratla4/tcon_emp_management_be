package com.tcon.empManagement.Entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "leave_counters")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class LeaveCounter {
    @Id
    private String id;
    private String empId;
    private int casualLeaves;
    private int sickLeaves;
    private double annualLeaves;     // Accrued monthly (.75 per month), up to 5 can be carried forward
    private List<String> publicHolidays;
    private int lossOfPayLeaves;     // Excess applied leaves deducted as loss of pay
}
