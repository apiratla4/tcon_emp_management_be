package com.tcon.empManagement.Repository;

import com.tcon.empManagement.Entity.AvailableEmpId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AvailableEmpIdRepository extends MongoRepository<AvailableEmpId, String> {

    Optional<AvailableEmpId> findFirstByOrderByNumericPartAsc();

    void deleteByEmpId(String empId);

    boolean existsByEmpId(String empId);
}

