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
@Document(collection = "client_documents")
public class ClientDocument {

    @Id
    private String id;

    @Field("business_name")
    private String businessName;

    @Field("project_name")
    private String projectName;

    @Field("contact_name")
    private String contactName; // Getting from ClientOnBoard table

    @Field("file_name")
    private String fileName;

    @Field("file_size")
    private Long fileSize;

    @Field("file_type")
    private String fileType;

    @Field("file_url")
    private String fileUrl;

    @Field("note")
    private String note;

    @Field("created_at")
    private LocalDateTime createdAt;

    @Field("updated_at")
    private LocalDateTime updatedAt;

    @Field("updated_by")
    private String updatedBy;
}
