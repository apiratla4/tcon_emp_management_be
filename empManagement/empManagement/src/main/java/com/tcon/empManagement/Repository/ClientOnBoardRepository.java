package com.tcon.empManagement.Repository;

import com.tcon.empManagement.Entity.ClientOnBoard;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientOnBoardRepository extends MongoRepository<ClientOnBoard, String> {

    Optional<ClientOnBoard> findByProjectId(String projectId);

    List<ClientOnBoard> findByClientInfo_BusinessName(String businessName);

    List<ClientOnBoard> findByContactInfo_ContactEmail(String contactEmail);
}
