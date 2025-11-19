package com.tcon.empManagement.Entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDate;

@Document(collection = "attendance")
@CompoundIndex(name = "unique_emp_date", def = "{'empId': 1, 'date': 1}", unique = true)
@Getter @Setter
@Builder
@AllArgsConstructor @NoArgsConstructor
public class Attendance {

    @Id
    private String id;

    private String empId;
    private String empName;
    private LocalDate date;        // yyyy-MM-dd

    private Instant checkIn;       // use Instant for Mongo
    private Instant checkOut;
    private String workMode;
    private String status;
    private Double workHours;
    private String empRole;

    private Instant createdAt;
    private Instant updatedAt;
}
