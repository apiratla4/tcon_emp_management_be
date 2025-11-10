package com.tcon.empManagement.Repository;

import com.tcon.empManagement.Entity.ClientOnBoard;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ClientOnBoardRepository extends MongoRepository<ClientOnBoard, String> {
    Optional<ClientOnBoard> findByProjectId(String projectId);
    List<ClientOnBoard> findAllByProjectId(String projectId);
    long deleteByProjectId(String projectId);
}
