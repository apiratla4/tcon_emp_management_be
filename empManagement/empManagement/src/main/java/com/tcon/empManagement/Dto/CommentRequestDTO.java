package com.tcon.empManagement.Dto;

import lombok.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentRequestDTO {


    private String storyId;


    @Size(min = 1, max = 5000, message = "Comment must be between 1 and 5000 characters")
    private String commentText;


    private String empId;
    private String employeeName;
    private String replyToEmpId;
    private String replyToEmployeeName;
    private String parentCommentId;
}
