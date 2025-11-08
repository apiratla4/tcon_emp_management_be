package com.tcon.empManagement.Repository;

import com.tcon.empManagement.Entity.AuthUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AuthUserRepository extends MongoRepository<AuthUser, String> {
    Optional<AuthUser> findByEmail(String email);
    Optional<AuthUser> findByEmpId(String empId);
    boolean existsByEmail(String email);
}
