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

import java.time.*;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository repo;
    private final static ZoneId DEFAULT_ZONE = ZoneId.of("Asia/Kolkata");

    @Override
    public AttendanceResponse checkIn(AttendanceCreateRequest req) {
        log.info("Check-in for empId={} date={}", req.getEmpId(), req.getDate());
        try {
            Instant now = Instant.now();

            Attendance attendance = Attendance.builder()
                    .empId(req.getEmpId())
                    .empName(req.getEmpName())
                    .date(req.getDate())
                    .checkIn(req.getCheckIn().toInstant())
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
                attendance.setCheckOut(req.getCheckOut().toInstant());

                if (attendance.getCheckIn() != null && attendance.getCheckOut() != null) {
                    Duration duration = Duration.between(attendance.getCheckIn(), attendance.getCheckOut());
                    double hours = duration.toMinutes() / 60.0;
                    attendance.setWorkHours(Math.round(hours * 100.0) / 100.0);
                    log.info("Work hours calculated: {} for id={}", attendance.getWorkHours(), id);
                }
            }
            if (req.getStatus() != null) {
                attendance.setStatus(req.getStatus());
            }
            attendance.setUpdatedAt(Instant.now());
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
        // For the whole day's range in IST:
        OffsetDateTime dayStart = date.atStartOfDay(DEFAULT_ZONE).toOffsetDateTime();
        OffsetDateTime dayEnd = date.plusDays(1).atStartOfDay(DEFAULT_ZONE).toOffsetDateTime().minusNanos(1);
        Instant utcStart = dayStart.toInstant();
        Instant utcEnd = dayEnd.toInstant();
        List<Attendance> records = repo.findByCheckInBetweenOrderByCheckInAsc(utcStart, utcEnd);
        return records.stream().map(this::mapToResponse).toList();
    }


    @Override
    public List<AttendanceResponse> getWeeklyTimesheet(String empId, LocalDate weekStart) {
        List<Attendance> records = repo.findByEmpIdAndDateBetween(empId, weekStart, weekStart.plusDays(6));
        Map<LocalDate, Attendance> map = records.stream().collect(java.util.stream.Collectors.toMap(Attendance::getDate, r -> r));
        List<AttendanceResponse> result = new ArrayList<>(7);
        for (int i = 0; i < 7; i++) {
            LocalDate cur = weekStart.plusDays(i);
            Attendance att = map.get(cur);

            String status;
            if (att != null) {
                status = att.getStatus();
            } else if (cur.getDayOfWeek() == DayOfWeek.SATURDAY || cur.getDayOfWeek() == DayOfWeek.SUNDAY) {
                status = "Weekoff";
            } else {
                status = "Absent";
            }

            result.add(AttendanceResponse.builder()
                    .id(att != null ? att.getId() : null)
                    .empId(empId)
                    .empName(att != null ? att.getEmpName() : null)
                    .date(cur)
                    .checkIn(att != null ? toOffsetDateTime(att.getCheckIn()) : null)
                    .checkOut(att != null ? toOffsetDateTime(att.getCheckOut()) : null)
                    .workMode(att != null ? att.getWorkMode() : null)
                    .status(status)
                    .workHours(att != null ? att.getWorkHours() : 0.0)
                    .empRole(att != null ? att.getEmpRole() : null)
                    .createdAt(att != null ? toOffsetDateTime(att.getCreatedAt()) : null)
                    .updatedAt(att != null ? toOffsetDateTime(att.getUpdatedAt()) : null)
                    .build());
        }
        return result;
    }

    private AttendanceResponse mapToResponse(Attendance a) {
        return AttendanceResponse.builder()
                .id(a.getId())
                .empId(a.getEmpId())
                .empName(a.getEmpName())
                .date(a.getDate())
                .checkIn(toOffsetDateTime(a.getCheckIn()))
                .checkOut(toOffsetDateTime(a.getCheckOut()))
                .workMode(a.getWorkMode())
                .status(a.getStatus())
                .workHours(a.getWorkHours())
                .empRole(a.getEmpRole())
                .createdAt(toOffsetDateTime(a.getCreatedAt()))
                .updatedAt(toOffsetDateTime(a.getUpdatedAt()))
                .build();
    }

    private OffsetDateTime toOffsetDateTime(Instant instant) {
        return instant == null ? null : instant.atZone(DEFAULT_ZONE).toOffsetDateTime();
    }
}
