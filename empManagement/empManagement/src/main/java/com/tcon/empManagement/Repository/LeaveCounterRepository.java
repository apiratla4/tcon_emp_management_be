package com.tcon.empManagement.Repository;

import com.tcon.empManagement.Entity.LeaveCounter;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface LeaveCounterRepository extends MongoRepository<LeaveCounter, String> {
    Optional<LeaveCounter> findByEmpId(String empId);
}
