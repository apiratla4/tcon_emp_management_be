package com.tcon.empManagement.Service;

import com.tcon.empManagement.Dto.AttendanceCreateRequest;
import com.tcon.empManagement.Dto.AttendanceResponse;
import com.tcon.empManagement.Dto.AttendanceUpdateRequest;
import com.tcon.empManagement.Entity.Attendance;
import com.tcon.empManagement.Repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository repo;

    @Override
    public AttendanceResponse checkIn(AttendanceCreateRequest req) {
        log.info("Check-in for empId={} date={}", req.getEmpId(), req.getDate());
        try {
            LocalDateTime now = LocalDateTime.now();

            Attendance attendance = Attendance.builder()
                    .empId(req.getEmpId())
                    .empName(req.getEmpName())
                    .date(req.getDate())
                    .checkIn(req.getCheckIn())
                    .workMode(req.getWorkMode())
                    .status(req.getStatus())
                    .empRole(req.getEmpRole())
                    .createdAt(now)
                    .updatedAt(now)
                    .build();

            Attendance saved = repo.save(attendance);
            log.info("Check-in successful id={} empId={}", saved.getId(), saved.getEmpId());
            return mapToResponse(saved);

        } catch (DuplicateKeyException ex) {
            log.error("Duplicate check-in for empId={} date={}", req.getEmpId(), req.getDate(), ex);
            throw new IllegalStateException("Attendance already recorded for this employee on " + req.getDate());
        } catch (Exception ex) {
            log.error("Check-in failed empId={}", req.getEmpId(), ex);
            throw ex;
        }
    }

    @Override
    public AttendanceResponse checkOut(String id, AttendanceUpdateRequest req) {
        log.info("Check-out for attendance id={}", id);
        try {
            Attendance attendance = repo.findById(id).orElseThrow(() -> {
                log.warn("Attendance not found id={}", id);
                return new NoSuchElementException("Attendance record not found: " + id);
            });

            if (req.getCheckOut() != null) {
                attendance.setCheckOut(req.getCheckOut());

                // Calculate work hours
                if (attendance.getCheckIn() != null && attendance.getCheckOut() != null) {
                    Duration duration = Duration.between(attendance.getCheckIn(), attendance.getCheckOut());
                    double hours = duration.toMinutes() / 60.0;
                    attendance.setWorkHours(Math.round(hours * 100.0) / 100.0); // Round to 2 decimals
                    log.info("Work hours calculated: {} for id={}", attendance.getWorkHours(), id);
                }
            }

            if (req.getStatus() != null) {
                attendance.setStatus(req.getStatus());
            }

            attendance.setUpdatedAt(LocalDateTime.now());
            Attendance saved = repo.save(attendance);
            log.info("Check-out successful id={}", saved.getId());
            return mapToResponse(saved);

        } catch (Exception ex) {
            log.error("Check-out failed id={}", id, ex);
            throw ex;
        }
    }
    @Override
    public Optional<Attendance> findByEmpIdAndDate(String empId, LocalDate date) {
        return repo.findByEmpIdAndDate(empId, date);
    }

    @Override
    public Attendance save(Attendance attendance) {
        return repo.save(attendance);
    }


    @Override
    public Page<AttendanceResponse> getAllAttendance(Pageable pageable) {
        log.info("Fetching all attendance page={} size={}", pageable.getPageNumber(), pageable.getPageSize());
        return repo.findAll(pageable).map(this::mapToResponse);
    }

    @Override
    public List<AttendanceResponse> getAttendanceByEmployee(String empId) {
        log.info("Fetching attendance for empId={}", empId);
        List<Attendance> records = repo.findByEmpIdOrderByDateDesc(empId);
        log.info("Found {} attendance records for empId={}", records.size(), empId);
        return records.stream().map(this::mapToResponse).toList();
    }

    @Override
    public AttendanceResponse getById(String id) {
        log.info("Fetching attendance id={}", id);
        return repo.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> {
                    log.warn("Attendance not found id={}", id);
                    return new NoSuchElementException("Attendance not found: " + id);
                });
    }

    @Override
    public void deleteById(String id) {
        log.info("Deleting attendance id={}", id);
        if (!repo.existsById(id)) {
            log.warn("Delete failed, not found id={}", id);
            throw new NoSuchElementException("Attendance not found: " + id);
        }
        repo.deleteById(id);
        log.info("Attendance deleted id={}", id);
    }

    @Override
    public List<AttendanceResponse> getAttendanceByDate(LocalDate date) {
        log.info("Fetching attendance for date={}", date);
        List<Attendance> records = repo.findByDateOrderByCheckInAsc(date);
        return records.stream()
                .map(this::mapToResponse)
                .toList();
    }
    private AttendanceResponse mapToResponse(Attendance a) {
        return AttendanceResponse.builder()
                .id(a.getId())
                .empId(a.getEmpId())
                .empName(a.getEmpName())
                .date(a.getDate())
                .checkIn(a.getCheckIn())
                .checkOut(a.getCheckOut())
                .workMode(a.getWorkMode())
                .status(a.getStatus())
                .workHours(a.getWorkHours())
                .empRole(a.getEmpRole())
                .createdAt(a.getCreatedAt())
                .updatedAt(a.getUpdatedAt())
                .build();
    }
}
