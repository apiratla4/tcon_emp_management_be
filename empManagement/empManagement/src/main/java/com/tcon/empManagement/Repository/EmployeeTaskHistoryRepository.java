package com.tcon.empManagement.Repository;



import com.tcon.empManagement.Entity.EmployeeTaskHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface EmployeeTaskHistoryRepository extends MongoRepository<EmployeeTaskHistory, String> {

    List<EmployeeTaskHistory> findByEmpIdOrderByCreatedAtDesc(String empId);

    List<EmployeeTaskHistory> findByStatusOrderByCreatedAtDesc(String status);

    Optional<EmployeeTaskHistory> findByIdAndEmpId(String id, String empId);
    long deleteByEmpId(String empId);
}
