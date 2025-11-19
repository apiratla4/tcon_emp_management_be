package com.tcon.empManagement.Entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "comments")
public class Comment {

    @Id
    private String id;

    @Field("story_id")
    private String storyId;

    @Field("comment_text")
    private String commentText;


    @Field("emp_id")
    private String empId;

    @Field("employee_name")
    private String employeeName;

    @Field("reply_to_emp_id")
    private String replyToEmpId;

    @Field("parent_comment_id")
    private String parentCommentId;

    private String replyToEmployeeName;

    @Field("replies")
    @Builder.Default
    private List<String> replies = new ArrayList<>();

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Field("created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Field("updated_at")
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Field("created_by")
    private String createdBy;

    @Field("updated_by")
    private String updatedBy;

    @Field("is_active")
    @Builder.Default
    private Boolean isActive = true;

    @Field("is_edited")
    @Builder.Default
    private Boolean isEdited = false;

    /**
     * Custom constructor for basic comment creation
     */
    public Comment(String storyId, String commentText, String empId) {
        this.storyId = storyId;
        this.commentText = commentText;
        this.empId = empId;
        this.createdBy = empId;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.isActive = true;
        this.isEdited = false;
        this.replies = new ArrayList<>();
    }

    /**
     * Add a reply to the replies list
     */
    public void addReply(String replyCommentId) {
        if (this.replies == null) {
            this.replies = new ArrayList<>();
        }
        this.replies.add(replyCommentId);
    }

    /**
     * Update comment text and mark as edited
     */
    public void updateCommentText(String newText) {
        this.commentText = newText;
        this.isEdited = true;
        this.updatedAt = LocalDateTime.now();
    }
}
