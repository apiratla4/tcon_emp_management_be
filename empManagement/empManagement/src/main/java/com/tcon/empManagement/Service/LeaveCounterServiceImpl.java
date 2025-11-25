package com.tcon.empManagement.Service;

import com.tcon.empManagement.Dto.LeaveCounterResponse;
import com.tcon.empManagement.Dto.LeaveCounterUpdateRequest;
import com.tcon.empManagement.Entity.LeaveCounter;
import com.tcon.empManagement.Repository.LeaveCounterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class LeaveCounterServiceImpl implements LeaveCounterService {

    private final LeaveCounterRepository repo;

    @Override
    public LeaveCounterResponse getByEmpId(String empId) {
        LeaveCounter counter = repo.findByEmpId(empId)
                .orElseThrow(() -> new NoSuchElementException("No leave counter found for empId=" + empId));
        return mapResponse(counter);
    }

    @Override
    public LeaveCounterResponse updateLeaveBalance(String empId, LeaveCounterUpdateRequest req) {
        LeaveCounter counter = repo.findByEmpId(empId)
                .orElseThrow(() -> new NoSuchElementException("No leave counter found for empId=" + empId));
        // Admin/HR can override balances
        counter.setCasualLeaves(req.getCasualLeaves());
        counter.setSickLeaves(req.getSickLeaves());
        counter.setAnnualLeaves(req.getAnnualLeaves());
        counter = repo.save(counter);
        return mapResponse(counter);
    }

    @Override
    public void deductLeave(String empId, String leaveType, int noOfDays) {
        LeaveCounter counter = repo.findByEmpId(empId)
                .orElseThrow(() -> new NoSuchElementException("No leave counter found for empId=" + empId));
        log.debug("Deducting {} days from {} leave for empId={}", noOfDays, leaveType, empId);
        switch (leaveType.trim().toUpperCase()) {
            case "CASUAL":
                counter.setCasualLeaves(Math.max(0, counter.getCasualLeaves() - noOfDays));
                break;
            case "SICK":
                counter.setSickLeaves(Math.max(0, counter.getSickLeaves() - noOfDays));
                break;
            case "ANNUAL":
                // Deduct, enforce 0 min and 5 max forwarded for next year
                int updatedAnnual = counter.getAnnualLeaves() - noOfDays;
                counter.setAnnualLeaves(Math.max(0, updatedAnnual));
                break;
            default:
                throw new IllegalArgumentException("Invalid leave type: " + leaveType);
        }
        repo.save(counter);
    }

    @Override
    public List<LeaveCounterResponse> getAllLeaveBalances() {
        return repo.findAll().stream().map(this::mapResponse).toList();
    }

    @Override
    public void addPublicHoliday(String date) {
        List<LeaveCounter> counters = repo.findAll();
        for (LeaveCounter lc : counters) {
            if (lc.getPublicHolidays() == null) lc.setPublicHolidays(new ArrayList<>());
            if (!lc.getPublicHolidays().contains(date)) {
                lc.getPublicHolidays().add(date);
                repo.save(lc);
            }
        }
    }

    @Override
    public List<String> getPublicHolidays() {
        List<LeaveCounter> counters = repo.findAll();
        if (counters.isEmpty() || counters.get(0).getPublicHolidays() == null) return Collections.emptyList();
        return counters.get(0).getPublicHolidays();
    }

    private LeaveCounterResponse mapResponse(LeaveCounter counter) {
        return LeaveCounterResponse.builder()
                .empId(counter.getEmpId())
                .casualLeaves(counter.getCasualLeaves())
                .sickLeaves(counter.getSickLeaves())
                .annualLeaves(counter.getAnnualLeaves())
                .publicHolidays(counter.getPublicHolidays())
                .build();
    }

    // Run this at YEAR END to handle carry-forward business logic
    public void carryForwardAnnualLeaves() {
        List<LeaveCounter> allCounters = repo.findAll();
        for (LeaveCounter lc : allCounters) {
            // Only carry maximum 5 unused annual leaves forward to next year
            int carryForward = Math.min(lc.getAnnualLeaves(), 5);
            lc.setAnnualLeaves(carryForward + 9); // add new year's annuals
            repo.save(lc);
        }
    }

    @Override
    public LeaveCounterResponse initializeCounter(String empId) {
        LeaveCounter lc = LeaveCounter.builder()
                .empId(empId)
                .casualLeaves(2)
                .sickLeaves(5)
                .annualLeaves(9)
                .publicHolidays(new ArrayList<>())
                .build();
        LeaveCounter saved = repo.save(lc);
        return mapResponse(saved); // <-- convert entity to DTO
    }

}
