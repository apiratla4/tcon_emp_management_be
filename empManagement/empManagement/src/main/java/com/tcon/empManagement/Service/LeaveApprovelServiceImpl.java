package com.tcon.empManagement.Service;


import com.tcon.empManagement.Dto.LeaveApprovelCreateRequest;
import com.tcon.empManagement.Dto.LeaveApprovelResponse;
import com.tcon.empManagement.Dto.LeaveApprovelUpdateStatusRequest;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class LeaveApprovelServiceImpl implements LeaveApprovelService {

    private final LeaveApprovelRepository repo;

    @Override
    public LeaveApprovelResponse applyLeave(LeaveApprovelCreateRequest req) {
        log.info("Applying leave for empId={} typeOfLeave={}", req.getEmpId(), req.getTypeOfLeave());
        // Status: default "PENDING"
        // noOfDays: Inclusive of both days (to-from)+1
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
        leave.setStatus(req.getStatus());
        leave.setStatusUpdateDate(LocalDateTime.now());
        LeaveApprovel saved = repo.save(leave);
        return mapResponse(saved);
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

    // Find leaves by status (dashboard, HR)
    @Override
    public List<LeaveApprovelResponse> getByStatus(String status) {
        log.info("Fetching leaves with status={}", status);
        return repo.findByStatusOrderByCreateDateDesc(status)
                .stream().map(this::mapResponse).toList();
    }

    // Get leaves for a date range (summary/statistics)
    @Override
    public List<LeaveApprovelResponse> getByDateRange(LocalDate from, LocalDate to) {
        log.info("Fetching leaves from {} to {}", from, to);
        return repo.findByFromDateGreaterThanEqualAndToDateLessThanEqualOrderByCreateDateDesc(from, to)
                .stream().map(this::mapResponse).toList();
    }

    // Delete/cancel leave request
    @Override
    public boolean deleteLeave(String id) {
        log.info("Deleting leave id={}", id);
        if (!repo.existsById(id)) return false;
        repo.deleteById(id);
        return true;
    }

    // Get a single leave request by ID
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
