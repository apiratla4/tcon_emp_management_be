package com.tcon.empManagement.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDocumentCreateDto {
    private String empId;
    private String empName;
    private String note;
    private String updatedBy;
}
