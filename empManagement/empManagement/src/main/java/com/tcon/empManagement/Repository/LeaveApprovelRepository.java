package com.tcon.empManagement.Repository;


import com.tcon.empManagement.Entity.LeaveApprovel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LeaveApprovelRepository extends MongoRepository<LeaveApprovel, String> {
    List<LeaveApprovel> findByEmpIdOrderByCreateDateDesc(String empId);
    List<LeaveApprovel> findByStatusOrderByCreateDateDesc(String status);
}
