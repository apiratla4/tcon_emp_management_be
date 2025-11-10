package com.tcon.empManagement.Repository;

import com.tcon.empManagement.Entity.FieldTable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FieldTableRepository extends MongoRepository<FieldTable, String> {

    List<FieldTable> findByTaskName(String taskName);

    List<FieldTable> findByPriority(String priority);

    List<FieldTable> findByType(String type);

    List<FieldTable> findByDeptName(String deptName);

    List<FieldTable> findByDeptNameAndPriority(String deptName, String priority);
}
