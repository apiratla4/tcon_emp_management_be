package com.tcon.empManagement.Repository;


import com.tcon.empManagement.Entity.CompanyDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyDocumentRepository extends MongoRepository<CompanyDocument, String> {

    // Find documents by organizationId
    List<CompanyDocument> findByOrganizationId(String organizationId);

    // Find document by DB id
    Optional<CompanyDocument> findById(String id);

    // Find documents by companyName
    List<CompanyDocument> findByCompanyName(String companyName);

    // Delete documents by organizationId
    void deleteByOrganizationId(String organizationId);

    // Delete document by id
    void deleteById(String id);
}
