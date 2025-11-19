package com.tcon.empManagement.Service;


import com.tcon.empManagement.Dto.CommentReplyDTO;
import com.tcon.empManagement.Dto.CommentRequestDTO;
import com.tcon.empManagement.Dto.CommentResponseDTO;
import com.tcon.empManagement.Entity.Comment;
import com.tcon.empManagement.Repository.CommentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for Comment operations
 * Handles all business logic for comment creation, retrieval, update, and deletion
 *
 * @author TCON Team
 * @version 1.0
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    /**
     * Create a new comment on a story
     */
    @Override
    public CommentResponseDTO createComment(CommentRequestDTO requestDTO) {
        log.info("Creating new comment for story: {} by employee: {}",
                requestDTO.getStoryId(), requestDTO.getEmpId());

        try {
            // Create comment entity using Builder pattern
            Comment comment = Comment.builder()
                    .storyId(requestDTO.getStoryId())
                    .commentText(requestDTO.getCommentText())
                    .empId(requestDTO.getEmpId())
                    .employeeName(requestDTO.getEmployeeName())
                    .replyToEmpId(requestDTO.getReplyToEmpId())
                    .replyToEmployeeName(requestDTO.getReplyToEmployeeName())
                    .parentCommentId(requestDTO.getParentCommentId())
                    .createdBy(requestDTO.getEmpId())
                    .updatedBy(requestDTO.getEmpId())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .isActive(true)
                    .isEdited(false)
                    .build();

            // Save comment to database
            Comment savedComment = commentRepository.save(comment);
            log.info("Comment created successfully with ID: {}", savedComment.getId());

            // If this is a reply, update parent comment's reply list
            if (requestDTO.getParentCommentId() != null &&
                    !requestDTO.getParentCommentId().isEmpty()) {
                updateParentCommentReplies(requestDTO.getParentCommentId(), savedComment.getId());
            }

            return convertToResponseDTO(savedComment);

        } catch (Exception e) {
            log.error("Error creating comment: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create comment: " + e.getMessage(), e);
        }
    }

    /**
     * Create a reply to an existing comment
     */
    @Override
    public CommentResponseDTO createReply(String parentCommentId, CommentRequestDTO requestDTO) {
        log.info("Creating reply to comment: {} by employee: {}",
                parentCommentId, requestDTO.getEmpId());

        // Verify parent comment exists
        Comment parentComment = commentRepository.findByIdAndIsActiveTrue(parentCommentId)
                .orElseThrow(() -> new RuntimeException("Parent comment not found: " + parentCommentId));

        // Set parent comment ID, reply-to employee, employee name, and story ID
        requestDTO.setParentCommentId(parentCommentId);
        requestDTO.setReplyToEmpId(parentComment.getEmpId());
        requestDTO.setReplyToEmployeeName(parentComment.getEmployeeName());
        requestDTO.setStoryId(parentComment.getStoryId());

        log.debug("Reply configured - Parent: {}, ReplyTo: {} ({}), Story: {}",
                parentCommentId, parentComment.getEmpId(), parentComment.getEmployeeName(), parentComment.getStoryId());

        return createComment(requestDTO);
    }

    /**
     * Get a comment by ID with all its replies
     */
    @Override
    public CommentResponseDTO getCommentById(String commentId) {
        log.info("Fetching comment by ID: {}", commentId);

        Comment comment = commentRepository.findByIdAndIsActiveTrue(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found: " + commentId));

        CommentResponseDTO responseDTO = convertToResponseDTO(comment);

        // Load all replies for this comment
        List<Comment> replies = commentRepository.findByParentCommentIdAndIsActiveTrue(commentId);
        responseDTO.setReplies(replies.stream()
                .map(this::convertToReplyDTO)
                .collect(Collectors.toList()));
        responseDTO.setReplyCount(replies.size());

        log.debug("Retrieved comment {} with {} replies", commentId, replies.size());

        return responseDTO;
    }

    /**
     * Get all comments for a specific story
     */
    @Override
    public List<CommentResponseDTO> getCommentsByStoryId(String storyId) {
        log.info("Fetching all comments for story: {}", storyId);

        List<Comment> comments = commentRepository.findByStoryIdAndIsActiveTrue(storyId);

        log.debug("Found {} comments for story: {}", comments.size(), storyId);

        return comments.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get all top-level comments for a story (without nested replies)
     */
    @Override
    public List<CommentResponseDTO> getTopLevelCommentsByStoryId(String storyId) {
        log.info("Fetching top-level comments for story: {}", storyId);

        List<Comment> topLevelComments = commentRepository.findTopLevelCommentsByStoryId(storyId);

        log.debug("Found {} top-level comments for story: {}", topLevelComments.size(), storyId);

        return topLevelComments.stream()
                .map(comment -> {
                    CommentResponseDTO dto = convertToResponseDTO(comment);
                    long replyCount = commentRepository.countByParentCommentIdAndIsActiveTrue(comment.getId());
                    dto.setReplyCount((int) replyCount);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * Get all replies for a specific comment
     */
    @Override
    public List<CommentResponseDTO> getRepliesByCommentId(String commentId) {
        log.info("Fetching replies for comment: {}", commentId);

        List<Comment> replies = commentRepository.findByParentCommentIdAndIsActiveTrue(commentId);

        log.debug("Found {} replies for comment: {}", replies.size(), commentId);

        return replies.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get all comments by a specific employee
     */
    @Override
    public List<CommentResponseDTO> getCommentsByEmployeeId(String empId) {
        log.info("Fetching comments by employee: {}", empId);

        List<Comment> comments = commentRepository.findByEmpIdAndIsActiveTrue(empId);

        log.debug("Found {} comments by employee: {}", comments.size(), empId);

        return comments.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get all comments replying to a specific employee
     */
    @Override
    public List<CommentResponseDTO> getCommentsReplyingToEmployee(String empId) {
        log.info("Fetching comments replying to employee: {}", empId);

        List<Comment> comments = commentRepository.findByReplyToEmpIdAndIsActiveTrue(empId);

        log.debug("Found {} comments replying to employee: {}", comments.size(), empId);

        return comments.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Update an existing comment
     */
    @Override
    public CommentResponseDTO updateComment(String commentId, CommentRequestDTO requestDTO) {
        log.info("Updating comment: {} by employee: {}", commentId, requestDTO.getEmpId());

        Comment comment = commentRepository.findByIdAndIsActiveTrue(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found: " + commentId));

        // Store original text for logging
        String originalText = comment.getCommentText();

        // Update comment text and metadata
        comment.setCommentText(requestDTO.getCommentText());
        comment.setUpdatedBy(requestDTO.getEmpId());
        comment.setUpdatedAt(LocalDateTime.now());
        comment.setIsEdited(true);

        Comment updatedComment = commentRepository.save(comment);

        log.info("Comment {} updated successfully. Length changed from {} to {} chars",
                commentId, originalText.length(), updatedComment.getCommentText().length());

        return convertToResponseDTO(updatedComment);
    }

    /**
     * Soft delete a comment and all its replies
     */
    @Override
    public void deleteComment(String commentId, String deletedBy) {
        log.info("Deleting comment: {} by user: {}", commentId, deletedBy);

        Comment comment = commentRepository.findByIdAndIsActiveTrue(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found: " + commentId));

        // Soft delete the main comment
        comment.setIsActive(false);
        comment.setUpdatedBy(deletedBy);
        comment.setUpdatedAt(LocalDateTime.now());

        commentRepository.save(comment);
        log.info("Comment {} soft deleted successfully", commentId);

        // Cascade soft delete to all replies
        List<Comment> replies = commentRepository.findByParentCommentIdAndIsActiveTrue(commentId);
        if (!replies.isEmpty()) {
            replies.forEach(reply -> {
                reply.setIsActive(false);
                reply.setUpdatedBy(deletedBy);
                reply.setUpdatedAt(LocalDateTime.now());
            });
            commentRepository.saveAll(replies);
            log.info("Cascade deleted {} replies for comment: {}", replies.size(), commentId);
        }
    }

    /**
     * Get comments within a date range
     */
    @Override
    public List<CommentResponseDTO> getCommentsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Fetching comments between {} and {}", startDate, endDate);

        List<Comment> comments = commentRepository.findByCreatedAtBetweenAndIsActiveTrue(startDate, endDate);

        log.debug("Found {} comments in date range", comments.size());

        return comments.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get total comment count for a story
     */
    @Override
    public long getCommentCountByStoryId(String storyId) {
        long count = commentRepository.countByStoryIdAndIsActiveTrue(storyId);
        log.debug("Story {} has {} comments", storyId, count);
        return count;
    }

    /**
     * Check if an employee has commented on a story
     */
    @Override
    public boolean hasEmployeeCommented(String storyId, String empId) {
        boolean hasCommented = commentRepository.existsByStoryIdAndEmpIdAndIsActiveTrue(storyId, empId);
        log.debug("Employee {} has{}commented on story {}", empId, hasCommented ? " " : " not ", storyId);
        return hasCommented;
    }

    // ==================== Private Helper Methods ====================

    /**
     * Update parent comment's reply list when a new reply is added
     */
    private void updateParentCommentReplies(String parentCommentId, String replyCommentId) {
        commentRepository.findByIdAndIsActiveTrue(parentCommentId)
                .ifPresent(parentComment -> {
                    parentComment.addReply(replyCommentId);
                    commentRepository.save(parentComment);
                    log.debug("Added reply {} to parent comment {}", replyCommentId, parentCommentId);
                });
    }

    /**
     * Convert Comment entity to CommentResponseDTO
     */
    private CommentResponseDTO convertToResponseDTO(Comment comment) {
        return CommentResponseDTO.builder()
                .id(comment.getId())
                .storyId(comment.getStoryId())
                .commentText(comment.getCommentText())
                .empId(comment.getEmpId())
                .employeeName(comment.getEmployeeName())
                .replyToEmpId(comment.getReplyToEmpId())
                .replyToEmployeeName(comment.getReplyToEmployeeName())
                .parentCommentId(comment.getParentCommentId())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .isEdited(comment.getIsEdited())
                .isActive(comment.getIsActive())
                .build();
    }

    /**
     * Convert Comment entity to CommentReplyDTO
     */
    private CommentReplyDTO convertToReplyDTO(Comment comment) {
        return CommentReplyDTO.builder()
                .id(comment.getId())
                .commentText(comment.getCommentText())
                .empId(comment.getEmpId())
                .employeeName(comment.getEmployeeName())
                .replyToEmpId(comment.getReplyToEmpId())
                .replyToEmployeeName(comment.getReplyToEmployeeName())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .isEdited(comment.getIsEdited())
                .build();
    }
}
