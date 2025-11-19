package com.tcon.empManagement.Service;


import com.tcon.empManagement.Dto.CommentRequestDTO;
import com.tcon.empManagement.Dto.CommentResponseDTO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service interface for Comment operations
 * Defines all business operations for managing comments and replies
 *
 * @author FineFlux Team
 * @version 1.0
 */
public interface CommentService {

    /**
     * Create a new comment on a story
     */
    CommentResponseDTO createComment(CommentRequestDTO requestDTO);

    /**
     * Create a reply to an existing comment
     */
    CommentResponseDTO createReply(String parentCommentId, CommentRequestDTO requestDTO);

    /**
     * Get a comment by ID with all its replies
     */
    CommentResponseDTO getCommentById(String commentId);

    /**
     * Get all comments for a specific story
     */
    List<CommentResponseDTO> getCommentsByStoryId(String storyId);

    /**
     * Get all top-level comments for a story (without replies)
     */
    List<CommentResponseDTO> getTopLevelCommentsByStoryId(String storyId);

    /**
     * Get all replies for a specific comment
     */
    List<CommentResponseDTO> getRepliesByCommentId(String commentId);

    /**
     * Get all comments by an employee
     */
    List<CommentResponseDTO> getCommentsByEmployeeId(String empId);

    /**
     * Get all comments where an employee was mentioned/replied to
     */
    List<CommentResponseDTO> getCommentsReplyingToEmployee(String empId);

    /**
     * Update a comment
     */
    CommentResponseDTO updateComment(String commentId, CommentRequestDTO requestDTO);

    /**
     * Soft delete a comment (set isActive to false)
     */
    void deleteComment(String commentId, String deletedBy);

    /**
     * Get comments within a date range
     */
    List<CommentResponseDTO> getCommentsByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Get comment count for a story
     */
    long getCommentCountByStoryId(String storyId);

    /**
     * Check if an employee has commented on a story
     */
    boolean hasEmployeeCommented(String storyId, String empId);
}
