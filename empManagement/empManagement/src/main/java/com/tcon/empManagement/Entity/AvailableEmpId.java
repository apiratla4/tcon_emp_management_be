package com.tcon.empManagement.Entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "available_empids")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AvailableEmpId {

    @Id
    private String id;

    private String empId;

    private Long numericPart;
}

