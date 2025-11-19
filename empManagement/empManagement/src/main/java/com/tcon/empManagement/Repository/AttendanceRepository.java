package com.tcon.empManagement.Repository;

import com.tcon.empManagement.Entity.Attendance;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends MongoRepository<Attendance, String> {
    List<Attendance> findByEmpIdOrderByDateDesc(String empId);
    Optional<Attendance> findByEmpIdAndDate(String empId, LocalDate date);
    List<Attendance> findByStatus(String status);

    List<Attendance> findByCheckInBetweenOrderByCheckInAsc(Instant start, Instant end);
    List<Attendance> findByEmpIdAndDateBetween(String empId, LocalDate start, LocalDate end);
}


