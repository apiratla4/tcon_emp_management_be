package com.tcon.empManagement.Dto;


import lombok.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponseDTO {

    private String id;
    private String storyId;
    private String commentText;
    private String empId;
    private String employeeName;
    private String replyToEmpId;
    private String replyToEmployeeName;
    private String parentCommentId;
    private List<CommentReplyDTO> replies;
    private Integer replyCount;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    private Boolean isEdited;
    private Boolean isActive;
}
