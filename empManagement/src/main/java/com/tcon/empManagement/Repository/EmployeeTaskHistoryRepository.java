package com.tcon.empManagement.Repository;



import com.tcon.empManagement.Entity.EmployeeTaskHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.Instant;
import java.util.List;

public interface EmployeeTaskHistoryRepository extends MongoRepository<EmployeeTaskHistory, String> {

    List<EmployeeTaskHistory> findByEmpIdOrderByCreatedAtDesc(String empId);

    List<EmployeeTaskHistory> findByStatusOrderByCreatedAtDesc(String status);

    List<EmployeeTaskHistory> findByEmpIdAndStatusOrderByCreatedAtDesc(String empId, String status);

    List<EmployeeTaskHistory> findByDueDateBetween(Instant from, Instant to);
}
