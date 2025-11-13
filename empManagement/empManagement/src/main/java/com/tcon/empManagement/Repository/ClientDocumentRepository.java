package com.tcon.empManagement.Repository;

import com.tcon.empManagement.Entity.ClientDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientDocumentRepository extends MongoRepository<ClientDocument, String> {

    // Find by businessName
    List<ClientDocument> findByBusinessName(String businessName);

    // Find by projectName
    List<ClientDocument> findByProjectName(String projectName);

    // Optionally for CRUD by ID
    Optional<ClientDocument> findById(String id);

    void deleteByBusinessName(String businessName);

    void deleteByProjectName(String projectName);

    void deleteById(String id);
}
