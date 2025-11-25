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
    // 2 casual/year
    private int casualLeaves;      // remaining casual leave for the year
    // 5 sick/year
    private int sickLeaves;        // remaining sick leave for the year
    // 9 annual/year, .75/month, up to 5 can be carried fwd
    private int annualLeaves;      // remaining annual leave for the year (with carry forward)
    private List<String> publicHolidays;
}
