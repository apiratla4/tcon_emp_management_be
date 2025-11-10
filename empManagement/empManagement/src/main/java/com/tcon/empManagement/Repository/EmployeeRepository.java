package com.tcon.empManagement.Repository;

import com.tcon.empManagement.Entity.Employee;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends MongoRepository<Employee, String> {
    // Find by unique fields
    Optional<Employee> findByEmail(String email);

    Optional<Employee> findByEmpId(String empId);
    // Find by status
    List<Employee> findByStatus(String status);
    boolean existsByEmpId(String empId);
    // Find by role
    List<Employee> findByEmpRole(String empRole);
    boolean existsByEmail(String email);
}
