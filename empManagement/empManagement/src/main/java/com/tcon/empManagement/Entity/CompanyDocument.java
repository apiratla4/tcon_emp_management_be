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
@Document(collection = "company_documents")
public class CompanyDocument {

    @Id
    private String id;

    @Field("company_name")
    private String companyName;

    @Field("organization_id")
    private String organizationId;

    @Field("date_of_issue")
    private LocalDateTime dateOfIssue;

    @Field("expiry_date")
    private LocalDateTime expiryDate;

    @Field("responsible_party")
    private String responsibleParty;

    @Field("document_name")
    private String documentName;

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
    private String updatedBy; // employeeId

    @Field("gcs_key")
    private String gcsKey; // Unique GCS blob name for this file
}
