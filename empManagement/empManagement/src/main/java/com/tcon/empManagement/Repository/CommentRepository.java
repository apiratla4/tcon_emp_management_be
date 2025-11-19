package com.tcon.empManagement.Repository;

import com.tcon.empManagement.Entity.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Comment entity
 * Provides data access methods for MongoDB Comment collection
 *
 * @author FineFlux Team
 * @version 1.0
 */
@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {


    List<Comment> findByStoryIdAndIsActiveTrue(String storyId);


    @Query("{ 'storyId': ?0, 'parentCommentId': null, 'isActive': true }")
    List<Comment> findTopLevelCommentsByStoryId(String storyId);


    List<Comment> findByParentCommentIdAndIsActiveTrue(String parentCommentId);


    List<Comment> findByEmpIdAndIsActiveTrue(String empId);


    List<Comment> findByReplyToEmpIdAndIsActiveTrue(String replyToEmpId);


    List<Comment> findByStoryIdAndEmpIdAndIsActiveTrue(String storyId, String empId);


    List<Comment> findByCreatedAtBetweenAndIsActiveTrue(LocalDateTime startDate, LocalDateTime endDate);


    long countByStoryIdAndIsActiveTrue(String storyId);


    long countByParentCommentIdAndIsActiveTrue(String parentCommentId);


    Optional<Comment> findByIdAndIsActiveTrue(String id);

    boolean existsByStoryIdAndEmpIdAndIsActiveTrue(String storyId, String empId);
}
