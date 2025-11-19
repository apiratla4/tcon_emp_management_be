package com.tcon.empManagement.Controller;

import com.tcon.empManagement.Dto.CommentRequestDTO;
import com.tcon.empManagement.Dto.CommentResponseDTO;
import com.tcon.empManagement.Service.CommentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for Comment operations
 * Provides endpoints for creating, reading, updating, and deleting comments
 *
 * @author TCON Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final CommentService commentService;

    /**
     * Create a new comment
     * POST /api/comments
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createComment(@Valid @RequestBody CommentRequestDTO requestDTO) {
        log.info("POST /api/comments - Creating new comment for story: {}", requestDTO.getStoryId());

        try {
            CommentResponseDTO response = commentService.createComment(requestDTO);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "Comment created successfully");
            responseMap.put("data", response);

            return ResponseEntity.status(HttpStatus.CREATED).body(responseMap);

        } catch (Exception e) {
            log.error("Error creating comment: {}", e.getMessage(), e);

            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("success", false);
            errorMap.put("message", "Failed to create comment: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
        }
    }

    /**
     * Create a reply to an existing comment
     * POST /api/comments/{commentId}/reply
     */
    @PostMapping("/{commentId}/reply")
    public ResponseEntity<Map<String, Object>> createReply(
            @PathVariable String commentId,
            @Valid @RequestBody CommentRequestDTO requestDTO) {

        log.info("POST /api/comments/{}/reply - Creating reply", commentId);

        try {
            CommentResponseDTO response = commentService.createReply(commentId, requestDTO);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "Reply created successfully");
            responseMap.put("data", response);

            return ResponseEntity.status(HttpStatus.CREATED).body(responseMap);

        } catch (Exception e) {
            log.error("Error creating reply: {}", e.getMessage(), e);

            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("success", false);
            errorMap.put("message", "Failed to create reply: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
        }
    }

    /**
     * Get comment by ID with all replies
     * GET /api/comments/{commentId}
     */
    @GetMapping("/{commentId}")
    public ResponseEntity<Map<String, Object>> getCommentById(@PathVariable String commentId) {
        log.info("GET /api/comments/{}", commentId);

        try {
            CommentResponseDTO response = commentService.getCommentById(commentId);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("data", response);

            return ResponseEntity.ok(responseMap);

        } catch (Exception e) {
            log.error("Error fetching comment: {}", e.getMessage(), e);

            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("success", false);
            errorMap.put("message", "Failed to fetch comment: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMap);
        }
    }

    /**
     * Get all comments for a story
     * GET /api/comments/story/{storyId}
     */
    @GetMapping("/story/{storyId}")
    public ResponseEntity<Map<String, Object>> getCommentsByStoryId(@PathVariable String storyId) {
        log.info("GET /api/comments/story/{}", storyId);

        try {
            List<CommentResponseDTO> comments = commentService.getCommentsByStoryId(storyId);
            long commentCount = commentService.getCommentCountByStoryId(storyId);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("count", commentCount);
            responseMap.put("data", comments);

            return ResponseEntity.ok(responseMap);

        } catch (Exception e) {
            log.error("Error fetching comments for story: {}", e.getMessage(), e);

            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("success", false);
            errorMap.put("message", "Failed to fetch comments: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
        }
    }

    /**
     * Get top-level comments for a story (without nested replies)
     * GET /api/comments/story/{storyId}/top-level
     */
    @GetMapping("/story/{storyId}/top-level")
    public ResponseEntity<Map<String, Object>> getTopLevelComments(@PathVariable String storyId) {
        log.info("GET /api/comments/story/{}/top-level", storyId);

        try {
            List<CommentResponseDTO> comments = commentService.getTopLevelCommentsByStoryId(storyId);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("data", comments);

            return ResponseEntity.ok(responseMap);

        } catch (Exception e) {
            log.error("Error fetching top-level comments: {}", e.getMessage(), e);

            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("success", false);
            errorMap.put("message", "Failed to fetch comments: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
        }
    }

    /**
     * Get all replies for a comment
     * GET /api/comments/{commentId}/replies
     */
    @GetMapping("/{commentId}/replies")
    public ResponseEntity<Map<String, Object>> getReplies(@PathVariable String commentId) {
        log.info("GET /api/comments/{}/replies", commentId);

        try {
            List<CommentResponseDTO> replies = commentService.getRepliesByCommentId(commentId);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("count", replies.size());
            responseMap.put("data", replies);

            return ResponseEntity.ok(responseMap);

        } catch (Exception e) {
            log.error("Error fetching replies: {}", e.getMessage(), e);

            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("success", false);
            errorMap.put("message", "Failed to fetch replies: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
        }
    }

    /**
     * Get all comments by an employee
     * GET /api/comments/employee/{empId}
     */
    @GetMapping("/employee/{empId}")
    public ResponseEntity<Map<String, Object>> getCommentsByEmployee(@PathVariable String empId) {
        log.info("GET /api/comments/employee/{}", empId);

        try {
            List<CommentResponseDTO> comments = commentService.getCommentsByEmployeeId(empId);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("count", comments.size());
            responseMap.put("data", comments);

            return ResponseEntity.ok(responseMap);

        } catch (Exception e) {
            log.error("Error fetching employee comments: {}", e.getMessage(), e);

            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("success", false);
            errorMap.put("message", "Failed to fetch comments: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
        }
    }

    /**
     * Get all comments replying to an employee
     * GET /api/comments/employee/{empId}/mentions
     */
    @GetMapping("/employee/{empId}/mentions")
    public ResponseEntity<Map<String, Object>> getEmployeeMentions(@PathVariable String empId) {
        log.info("GET /api/comments/employee/{}/mentions", empId);

        try {
            List<CommentResponseDTO> comments = commentService.getCommentsReplyingToEmployee(empId);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("count", comments.size());
            responseMap.put("data", comments);

            return ResponseEntity.ok(responseMap);

        } catch (Exception e) {
            log.error("Error fetching employee mentions: {}", e.getMessage(), e);

            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("success", false);
            errorMap.put("message", "Failed to fetch mentions: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
        }
    }

    /**
     * Update a comment
     * PUT /api/comments/{commentId}
     */
    @PutMapping("/{commentId}")
    public ResponseEntity<Map<String, Object>> updateComment(
            @PathVariable String commentId,
            @Valid @RequestBody CommentRequestDTO requestDTO) {

        log.info("PUT /api/comments/{}", commentId);

        try {
            CommentResponseDTO response = commentService.updateComment(commentId, requestDTO);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "Comment updated successfully");
            responseMap.put("data", response);

            return ResponseEntity.ok(responseMap);

        } catch (Exception e) {
            log.error("Error updating comment: {}", e.getMessage(), e);

            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("success", false);
            errorMap.put("message", "Failed to update comment: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
        }
    }

    /**
     * Delete a comment (soft delete)
     * DELETE /api/comments/{commentId}
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Map<String, Object>> deleteComment(
            @PathVariable String commentId,
            @RequestParam String deletedBy) {

        log.info("DELETE /api/comments/{} by user: {}", commentId, deletedBy);

        try {
            commentService.deleteComment(commentId, deletedBy);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "Comment deleted successfully");

            return ResponseEntity.ok(responseMap);

        } catch (Exception e) {
            log.error("Error deleting comment: {}", e.getMessage(), e);

            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("success", false);
            errorMap.put("message", "Failed to delete comment: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
        }
    }

    /**
     * Get comments by date range
     * GET /api/comments/date-range
     */
    @GetMapping("/date-range")
    public ResponseEntity<Map<String, Object>> getCommentsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        log.info("GET /api/comments/date-range from {} to {}", startDate, endDate);

        try {
            List<CommentResponseDTO> comments = commentService.getCommentsByDateRange(startDate, endDate);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("count", comments.size());
            responseMap.put("data", comments);

            return ResponseEntity.ok(responseMap);

        } catch (Exception e) {
            log.error("Error fetching comments by date range: {}", e.getMessage(), e);

            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("success", false);
            errorMap.put("message", "Failed to fetch comments: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
        }
    }

    /**
     * Check if employee has commented on a story
     * GET /api/comments/check
     */
    @GetMapping("/check")
    public ResponseEntity<Map<String, Object>> checkEmployeeComment(
            @RequestParam String storyId,
            @RequestParam String empId) {

        log.info("GET /api/comments/check - storyId: {}, empId: {}", storyId, empId);

        try {
            boolean hasCommented = commentService.hasEmployeeCommented(storyId, empId);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("hasCommented", hasCommented);

            return ResponseEntity.ok(responseMap);

        } catch (Exception e) {
            log.error("Error checking employee comment: {}", e.getMessage(), e);

            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("success", false);
            errorMap.put("message", "Failed to check comment: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
        }
    }
}
