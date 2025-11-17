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
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LeaveApprovelServiceImpl implements LeaveApprovelService {

    private final LeaveApprovelRepository repo;
    private final AttendanceService attendanceService; // This should provide findByEmpIdAndDate and save

    @Override
    public LeaveApprovelResponse applyLeave(LeaveApprovelCreateRequest req) {
        log.info("Applying leave for empId={} typeOfLeave={}", req.getEmpId(), req.getTypeOfLeave());
        double noOfDays = ChronoUnit.DAYS.between(req.getFromDate(), req.getToDate()) + 1;
        LeaveApprovel entity = LeaveApprovel.builder()
                .empId(req.getEmpId())
                .empName(req.getEmpName())
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

        // On approval, update Attendance status for the leave dates
        if ("APPROVED".equalsIgnoreCase(req.getStatus())) {
            for (LocalDate date = leave.getFromDate(); !date.isAfter(leave.getToDate()); date = date.plusDays(1)) {
                Optional<Attendance> optionalAttendance = attendanceService.findByEmpIdAndDate(leave.getEmpId(), date);
                Attendance attendance = optionalAttendance.orElse(Attendance.builder()
                        .empId(leave.getEmpId())
                        .empName(leave.getEmpName())
                        .date(date)
                        .status("OnLeave")
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build());
                attendance.setStatus("OnLeave");
                attendance.setUpdatedAt(LocalDateTime.now());
                attendanceService.save(attendance);
            }
        }

        leave.setStatus(req.getStatus());
        leave.setStatusUpdateDate(LocalDateTime.now());
        LeaveApprovel updated = repo.save(leave);
        return mapResponse(updated);
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

    private LeaveApprovelResponse mapResponse(LeaveApprovel l) {
        return LeaveApprovelResponse.builder()
                .id(l.getId())
                .empId(l.getEmpId())
                .empName(l.getEmpName())
                .typeOfLeave(l.getTypeOfLeave())
                .fromDate(l.getFromDate())
                .toDate(l.getToDate())
                .noOfDays(l.getNoOfDays())
                .reason(l.getReason())
                .status(l.getStatus())
                .createDate(l.getCreateDate())
                .statusUpdateDate(l.getStatusUpdateDate())
                .build();
    }
}
