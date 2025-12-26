package com.tcon.empManagement.Service;

import com.tcon.empManagement.Dto.LeaveApprovelCreateRequest;
import com.tcon.empManagement.Dto.LeaveApprovelResponse;
import com.tcon.empManagement.Dto.LeaveApprovelUpdateStatusRequest;
import com.tcon.empManagement.Entity.Attendance;
import com.tcon.empManagement.Entity.LeaveApprovel;
import com.tcon.empManagement.Repository.LeaveApprovelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LeaveApprovelServiceImpl implements LeaveApprovelService {

    private final LeaveApprovelRepository repo;
    private final AttendanceService attendanceService;
    private final LeaveCounterService leaveCounterService;
    @Override
    public LeaveApprovelResponse applyLeave(LeaveApprovelCreateRequest req) {
        log.info("Applying leave for empId={} typeOfLeave={}", req.getEmpId(), req.getTypeOfLeave());
        double noOfDays = ChronoUnit.DAYS.between(req.getFromDate(), req.getToDate()) + 1;
        LeaveApprovel entity = LeaveApprovel.builder()
                .empId(req.getEmpId())
                .empName(req.getEmpName())
                .empRole(req.getEmpRole())
                .typeOfLeave(req.getTypeOfLeave())
                .fromDate(req.getFromDate())
                .toDate(req.getToDate())
                .noOfDays(noOfDays)
                .reason(req.getReason())
                .status("PENDING")
                .createDate(LocalDateTime.now())
                .statusUpdateDate(null)
                .build();
        LeaveApprovel saved = repo.save(entity);
        return mapResponse(saved);
    }

    @Override
    public LeaveApprovelResponse updateStatus(String id, LeaveApprovelUpdateStatusRequest req) {
        log.info("Updating leave status id={} status={}", id, req.getStatus());
        LeaveApprovel leave = repo.findById(id).orElseThrow(
                () -> new NoSuchElementException("LeaveApprovel not found: " + id)
        );

        String previousStatus = leave.getStatus();
        String newStatus = req.getStatus();
        int days = (leave.getNoOfDays() != null) ? leave.getNoOfDays().intValue() : 0;

        // Map leave type to canonical form
        String mappedType = null;
        String orig = leave.getTypeOfLeave();
        if (orig != null && days > 0) {
            String norm = orig.trim().toUpperCase();
            if (norm.contains("CASUAL")) mappedType = "CASUAL";
            else if (norm.contains("SICK")) mappedType = "SICK";
            else if (norm.contains("ANNUAL") || norm.contains("VACATION")) mappedType = "ANNUAL";
            else throw new IllegalArgumentException("Invalid leave type: " + orig);
        }

        // Handle leave counter based on status transition
        if (mappedType != null && days > 0) {
            boolean wasApproved = "APPROVED".equalsIgnoreCase(previousStatus);
            boolean isApproved = "APPROVED".equalsIgnoreCase(newStatus);

            if (!wasApproved && isApproved) {
                // Transitioning TO approved: DEDUCT leave
                log.info("Deducting {} days of {} leave for empId={}", days, mappedType, leave.getEmpId());
                leaveCounterService.deductLeave(leave.getEmpId(), mappedType, days);

                // Update attendance for approved dates
                for (LocalDate date = leave.getFromDate(); !date.isAfter(leave.getToDate()); date = date.plusDays(1)) {
                    Optional<Attendance> optionalAttendance = attendanceService.findByEmpIdAndDate(leave.getEmpId(), date);
                    Attendance attendance = optionalAttendance.orElse(Attendance.builder()
                            .empId(leave.getEmpId())
                            .empName(leave.getEmpName())
                            .date(date)
                            .status("OnLeave")
                            .createdAt(OffsetDateTime.now().toInstant())
                            .updatedAt(OffsetDateTime.now().toInstant())
                            .build());
                    attendance.setStatus("OnLeave");
                    attendance.setUpdatedAt(OffsetDateTime.now().toInstant());
                    attendanceService.save(attendance);
                }
            } else if (wasApproved && !isApproved) {
                // Transitioning FROM approved to rejected or pending: RESTORE leave
                log.info("Restoring {} days of {} leave for empId={}", days, mappedType, leave.getEmpId());
                restoreLeave(leave.getEmpId(), mappedType, days);
            }
        }

        if ("REJECTED".equalsIgnoreCase(newStatus)) {
            leave.setRejectReason(req.getRejectReason());
        } else {
            // Clear rejectReason if status changes away from REJECTED
            leave.setRejectReason(null);
        }

        leave.setStatus(req.getStatus());
        leave.setStatusUpdateDate(LocalDateTime.now());
        LeaveApprovel updated = repo.save(leave);
        return mapResponse(updated);
    }

    // Add this new helper method in the same class
    private void restoreLeave(String empId, String leaveType, int days) {
        // Add the restore logic in LeaveCounterServiceImpl (shown below)
        leaveCounterService.restoreLeave(empId, leaveType, days);
    }

    @Override
    public List<LeaveApprovelResponse> getByEmpId(String empId) {
        log.info("Fetching leaves for empId={}", empId);
        return repo.findByEmpIdOrderByCreateDateDesc(empId)
                .stream().map(this::mapResponse).toList();
    }

    @Override
    public Page<LeaveApprovelResponse> getAll(Pageable pageable) {
        log.info("Fetching paginated leaves");
        return repo.findAll(pageable).map(this::mapResponse);
    }

    @Override
    public List<LeaveApprovelResponse> getByStatus(String status) {
        log.info("Fetching leaves with status={}", status);
        return repo.findByStatusOrderByCreateDateDesc(status)
                .stream().map(this::mapResponse).toList();
    }

    @Override
    public List<LeaveApprovelResponse> getByDateRange(LocalDate from, LocalDate to) {
        log.info("Fetching leaves from {} to {}", from, to);
        return repo.findByFromDateGreaterThanEqualAndToDateLessThanEqualOrderByCreateDateDesc(from, to)
                .stream().map(this::mapResponse).toList();
    }

    @Override
    public boolean deleteLeave(String id) {
        log.info("Deleting leave id={}", id);
        if (!repo.existsById(id)) return false;
        repo.deleteById(id);
        return true;
    }

    @Override
    public LeaveApprovelResponse getById(String id) {
        log.info("Fetching leave by id={}", id);
        LeaveApprovel leave = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("LeaveApprovel not found: " + id));
        return mapResponse(leave);
    }

    @Override
    public List<LeaveApprovelResponse> getLeavesForRole(String role, String currentEmpId) {
        log.info("Fetching leaves visible for role={} currentEmpId={}", role, currentEmpId);

        if (role == null) return List.of();
        String normalized = role.trim().toUpperCase(Locale.ROOT);

        // 1) Load all
        List<LeaveApprovel> all = repo.findAll();
        log.info("Total leave rows in DB = {}", all.size());

        for (LeaveApprovel l : all) {
            log.info("Row: id={} empId={} empRole={} status={}",
                    l.getId(), l.getEmpId(), l.getEmpRole(), l.getStatus());
        }

        // 2) VERY SIMPLE filter first so you see data in Postman
        List<LeaveApprovel> filtered;

        switch (normalized) {
            case "CEO":
                // CEO: see everything for now (including own) – frontend blocks self‑approve
                filtered = all;
                break;

            case "HR":
                // HR: see everything for now, we can tighten later
                filtered = all;
                break;

            case "MANAGER":
                // Manager: see everything for now, frontend will still only allow
                // approving EMPLOYEE and not own
                filtered = all;
                break;

            default:
                filtered = List.of();
        }

        log.info("Filtered size for role {} = {}", normalized, filtered.size());

        return filtered.stream()
                .map(this::mapResponse)
                .toList();
    }


    @Override
    public LeaveApprovelResponse updateLeave(String id, LeaveApprovelCreateRequest req) {
        log.info("Updating leave details id={} empId={}", id, req.getEmpId());

        LeaveApprovel leave = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("LeaveApprovel not found: " + id));

        // Only allow updates on PENDING status
        if (!"PENDING".equalsIgnoreCase(leave.getStatus())) {
            throw new IllegalStateException("Cannot update leave with status: " + leave.getStatus());
        }

        // Verify employee owns this request
        if (!req.getEmpId().equals(leave.getEmpId())) {
            throw new IllegalArgumentException("Cannot update another employee's leave request");
        }

        // Update fields
        double noOfDays = ChronoUnit.DAYS.between(req.getFromDate(), req.getToDate()) + 1;
        leave.setEmpName(req.getEmpName());
        leave.setEmpRole(req.getEmpRole());
        leave.setTypeOfLeave(req.getTypeOfLeave());
        leave.setFromDate(req.getFromDate());
        leave.setToDate(req.getToDate());
        leave.setNoOfDays(noOfDays);
        leave.setReason(req.getReason());

        LeaveApprovel updated = repo.save(leave);
        return mapResponse(updated);
    }


    private LeaveApprovelResponse mapResponse(LeaveApprovel l) {
        return LeaveApprovelResponse.builder()
                .id(l.getId())
                .empId(l.getEmpId())
                .empName(l.getEmpName())
                .empRole(l.getEmpRole())
                .typeOfLeave(l.getTypeOfLeave())
                .fromDate(l.getFromDate())
                .toDate(l.getToDate())
                .noOfDays(l.getNoOfDays())
                .reason(l.getReason())
                .status(l.getStatus())
                .createDate(l.getCreateDate())
                .statusUpdateDate(l.getStatusUpdateDate())
                .rejectReason(l.getRejectReason())
                .build();
    }
}
