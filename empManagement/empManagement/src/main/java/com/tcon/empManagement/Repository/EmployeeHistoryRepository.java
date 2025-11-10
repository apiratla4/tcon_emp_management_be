package com.tcon.empManagement.Repository;

import com.tcon.empManagement.Entity.EmployeeHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface EmployeeHistoryRepository extends MongoRepository<EmployeeHistory, String> {

    List<EmployeeHistory> findByEmployeeId(String employeeId);

    List<EmployeeHistory> findByEmpId(String empId);

    List<EmployeeHistory> findByOperation(String operation);

    List<EmployeeHistory> findByStatus(String status);

    List<EmployeeHistory> findByOperationTimeBetween(Instant start, Instant end);

    List<EmployeeHistory> findByEmployeeIdOrderByOperationTimeDesc(String employeeId);

    List<EmployeeHistory> findByChangedBy(String changedBy);
}
