/*
package com.tcon.empManagement.Repository;

import com.tcon.empManagement.Entity.EmployeeDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeDocumentRepository extends MongoRepository<EmployeeDocument, String> {

    List<EmployeeDocument> findByEmpId(String empId);

    Optional<EmployeeDocument> findByFileName(String fileName);

    @Query("{'emp_id': ?0, 'file_name': ?1}")
    Optional<EmployeeDocument> findByEmpIdAndFileName(String empId, String fileName);

    boolean existsByEmpIdAndFileName(String empId, String fileName);

    void deleteByEmpId(String empId);

    // Count documents by employee
    long countByEmpId(String empId);
}
*/
