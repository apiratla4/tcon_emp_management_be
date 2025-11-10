package com.tcon.empManagement.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "field_table")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FieldTable {

    @Id
    private String id;

    private String taskName;

    private String taskDescription;

    private String priority; // HIGH, MEDIUM, LOW

    private String type;

    private String deptName;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
