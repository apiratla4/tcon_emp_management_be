package com.tcon.empManagement.Repository;


import com.tcon.empManagement.Entity.EmployeeAttendanceSummary;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface EmployeeAttendanceSummaryRepository extends MongoRepository<EmployeeAttendanceSummary, String> {
    Optional<EmployeeAttendanceSummary> findByEmpId(String empId);
}
