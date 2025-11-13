package com.tcon.empManagement.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "employee_documents")
public class EmployeeDocument {

    @Id
    private String id;

    @Field("emp_id")
    private String empId;

    @Field("emp_name")
    private String empName; // firstName + lastName from Employee table

    @Field("file_name")
    private String fileName;

    @Field("file_size")
    private Long fileSize; // in bytes

    @Field("file_url")
    private String fileUrl;

    @Field("note")
    private String note;

    @Field("created_at")
    private LocalDateTime createdAt;

    @Field("updated_at")
    private LocalDateTime updatedAt;

    @Field("updated_by")
    private String updatedBy; // Employee ID who updated
}
