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
        try {
            LeaveCounter counter = repo.findByEmpId(empId)
                    .orElseThrow(() -> new NoSuchElementException("No leave counter found for empId=" + empId));
            log.info("Retrieved leave counter for empId={}", empId);
            return mapResponse(counter);
        } catch (Exception e) {
            log.error("Error retrieving leave counter for empId={}: {}", empId, e.getMessage());
            throw e;
        }
    }

    @Override
    public LeaveCounterResponse updateLeaveBalance(String empId, LeaveCounterUpdateRequest req) {
        try {
            LeaveCounter counter = repo.findByEmpId(empId)
                    .orElseThrow(() -> new NoSuchElementException("No leave counter found for empId=" + empId));

            counter.setCasualLeaves(req.getCasualLeaves());
            counter.setSickLeaves(req.getSickLeaves());
            counter.setAnnualLeaves(req.getAnnualLeaves());
            counter.setLossOfPayLeaves(req.getLossOfPayLeaves());

            counter = repo.save(counter);
            log.info("Updated leave balance for empId={}: Casual={}, Sick={}, Annual={}, LOP={}",
                    empId, counter.getCasualLeaves(), counter.getSickLeaves(),
                    counter.getAnnualLeaves(), counter.getLossOfPayLeaves());
            return mapResponse(counter);
        } catch (Exception e) {
            log.error("Error updating leave balance for empId={}: {}", empId, e.getMessage());
            throw e;
        }
    }

    @Override
    public void deductLeave(String empId, String leaveType, int noOfDays) {
        try {
            LeaveCounter counter = repo.findByEmpId(empId)
                    .orElseThrow(() -> new NoSuchElementException("No leave counter found for empId=" + empId));

            log.debug("Deducting {} days from {} leave for empId={}", noOfDays, leaveType, empId);
            log.debug("Current balances - Casual: {}, Sick: {}, Annual: {}, LOP: {}",
                    counter.getCasualLeaves(), counter.getSickLeaves(),
                    counter.getAnnualLeaves(), counter.getLossOfPayLeaves());

            int excess = 0;
            switch (leaveType.trim().toUpperCase()) {
                case "CASUAL":
                    if (noOfDays > counter.getCasualLeaves()) {
                        excess = noOfDays - counter.getCasualLeaves();
                        counter.setCasualLeaves(0);
                        log.warn("Insufficient casual leaves for empId={}. Required: {}, Available: {}. Excess {} days marked as LOP.",
                                empId, noOfDays, counter.getCasualLeaves() + excess, excess);
                    } else {
                        counter.setCasualLeaves(counter.getCasualLeaves() - noOfDays);
                    }
                    break;

                case "SICK":
                    if (noOfDays > counter.getSickLeaves()) {
                        excess = noOfDays - counter.getSickLeaves();
                        counter.setSickLeaves(0);
                        log.warn("Insufficient sick leaves for empId={}. Required: {}, Available: {}. Excess {} days marked as LOP.",
                                empId, noOfDays, counter.getSickLeaves() + excess, excess);
                    } else {
                        counter.setSickLeaves(counter.getSickLeaves() - noOfDays);
                    }
                    break;

                case "ANNUAL":
                    if (noOfDays > counter.getAnnualLeaves()) {
                        // Properly handle decimal annual leaves
                        excess = (int) Math.ceil(noOfDays - counter.getAnnualLeaves());
                        counter.setAnnualLeaves(0);
                        log.warn("Insufficient annual leaves for empId={}. Required: {}, Available: {}. Excess {} days marked as LOP.",
                                empId, noOfDays, counter.getAnnualLeaves(), excess);
                    } else {
                        counter.setAnnualLeaves(counter.getAnnualLeaves() - noOfDays);
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Invalid leave type: " + leaveType + ". Valid types: CASUAL, SICK, ANNUAL");
            }

            // Apply Loss of Pay for excess days
            if (excess > 0) {
                counter.setLossOfPayLeaves(counter.getLossOfPayLeaves() + excess);
                log.info("Loss of Pay applied: {} days added for empId={}. New LOP total: {}",
                        excess, empId, counter.getLossOfPayLeaves());
            }

            repo.save(counter);
            log.info("Leave deducted successfully for empId={}. New balances - Casual: {}, Sick: {}, Annual: {}, LOP: {}",
                    empId, counter.getCasualLeaves(), counter.getSickLeaves(),
                    counter.getAnnualLeaves(), counter.getLossOfPayLeaves());

        } catch (NoSuchElementException | IllegalArgumentException e) {
            log.error("Error deducting leave for empId={}: {}", empId, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error deducting leave for empId={}: {}", empId, e.getMessage(), e);
            throw new RuntimeException("Failed to deduct leave for empId=" + empId, e);
        }
    }

    @Override
    public List<LeaveCounterResponse> getAllLeaveBalances() {
        try {
            List<LeaveCounter> counters = repo.findAll();
            log.info("Retrieved {} leave counter records", counters.size());
            return counters.stream().map(this::mapResponse).toList();
        } catch (Exception e) {
            log.error("Error retrieving all leave balances: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public void addPublicHoliday(String date) {
        try {
            List<LeaveCounter> counters = repo.findAll();
            List<LeaveCounter> updatedCounters = new ArrayList<>();

            for (LeaveCounter lc : counters) {
                if (lc.getPublicHolidays() == null) {
                    lc.setPublicHolidays(new ArrayList<>());
                }
                if (!lc.getPublicHolidays().contains(date)) {
                    lc.getPublicHolidays().add(date);
                    updatedCounters.add(lc);
                }
            }

            // Batch save for better performance
            if (!updatedCounters.isEmpty()) {
                repo.saveAll(updatedCounters);
                log.info("Public holiday '{}' added to {} employee records", date, updatedCounters.size());
            } else {
                log.info("Public holiday '{}' already exists for all employees", date);
            }
        } catch (Exception e) {
            log.error("Error adding public holiday '{}': {}", date, e.getMessage());
            throw e;
        }
    }

    @Override
    public List<String> getPublicHolidays() {
        try {
            List<LeaveCounter> counters = repo.findAll();
            if (counters.isEmpty() || counters.get(0).getPublicHolidays() == null) {
                log.info("No public holidays found");
                return Collections.emptyList();
            }
            log.info("Retrieved {} public holidays", counters.get(0).getPublicHolidays().size());
            return counters.get(0).getPublicHolidays();
        } catch (Exception e) {
            log.error("Error retrieving public holidays: {}", e.getMessage());
            throw e;
        }
    }

    private LeaveCounterResponse mapResponse(LeaveCounter counter) {
        return LeaveCounterResponse.builder()
                .empId(counter.getEmpId())
                .casualLeaves(counter.getCasualLeaves())
                .sickLeaves(counter.getSickLeaves())
                .annualLeaves(counter.getAnnualLeaves())
                .publicHolidays(counter.getPublicHolidays() != null ? counter.getPublicHolidays() : Collections.emptyList())
                .lossOfPayLeaves(counter.getLossOfPayLeaves())
                .build();
    }

    // ---- ANNUAL LEAVE ACCRUAL AND CARRY FORWARD ----

    /**
     * Accrues 0.75 annual leave per employee per month.
     * Should be scheduled to run monthly (e.g., using @Scheduled annotation).
     */
    @Override
    public void accrueAnnualLeaveMonthly() {
        try {
            List<LeaveCounter> allCounters = repo.findAll();
            for (LeaveCounter lc : allCounters) {
                lc.setAnnualLeaves(lc.getAnnualLeaves() + 0.75);
            }
            // Batch save for performance optimization
            repo.saveAll(allCounters);
            log.info("Monthly annual leave accrual completed for {} employees. Added 0.75 leaves each.", allCounters.size());
        } catch (Exception e) {
            log.error("Error during monthly annual leave accrual: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to accrue annual leaves", e);
        }
    }

    /**
     * Carries forward up to 5 annual leaves to next year, discards excess.
     * Should be run at year end (e.g., December 31st using @Scheduled annotation).
     */
    @Override
    public void carryForwardAnnualLeaves() {
        try {
            List<LeaveCounter> allCounters = repo.findAll();
            int totalEmployees = allCounters.size();
            int employeesWithCarryForward = 0;

            for (LeaveCounter lc : allCounters) {
                double currentAnnualLeaves = lc.getAnnualLeaves();
                double carryForward = Math.min(currentAnnualLeaves, 5.0);
                lc.setAnnualLeaves(carryForward);

                if (currentAnnualLeaves > 5.0) {
                    employeesWithCarryForward++;
                    log.debug("Employee {} had {} annual leaves. Carried forward: 5.0, Forfeited: {}",
                            lc.getEmpId(), currentAnnualLeaves, currentAnnualLeaves - 5.0);
                }
            }

            // Batch save for performance optimization
            repo.saveAll(allCounters);
            log.info("Year-end carry forward completed. Total employees: {}, Employees with forfeited leaves: {}",
                    totalEmployees, employeesWithCarryForward);
        } catch (Exception e) {
            log.error("Error during year-end carry forward: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to carry forward annual leaves", e);
        }
    }

    // ---- CREATE/INIT COUNTER ----

    /**
     * Initializes leave counter for a new employee with default values.
     * Casual Leaves: 2, Sick Leaves: 5, Annual Leaves: 0 (accrued monthly).
     */
    @Override
    public LeaveCounterResponse initializeCounter(String empId) {
        try {
            // Check if counter already exists
            if (repo.findByEmpId(empId).isPresent()) {
                log.warn("Leave counter already exists for empId={}. Skipping initialization.", empId);
                throw new IllegalStateException("Leave counter already exists for empId=" + empId);
            }

            LeaveCounter lc = LeaveCounter.builder()
                    .empId(empId)
                    .casualLeaves(2)
                    .sickLeaves(5)
                    .annualLeaves(0.0) // Start at 0, accrued monthly at 0.75
                    .lossOfPayLeaves(0)
                    .publicHolidays(new ArrayList<>())
                    .build();

            LeaveCounter saved = repo.save(lc);
            log.info("Leave counter initialized for empId={} with default values: Casual=2, Sick=5, Annual=0.0, LOP=0", empId);
            return mapResponse(saved);
        } catch (IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error initializing leave counter for empId={}: {}", empId, e.getMessage(), e);
            throw new RuntimeException("Failed to initialize leave counter for empId=" + empId, e);
        }
    }

    /**
     * Restores leave balance when a leave application is rejected or cancelled.
     */
    @Override
    public void restoreLeave(String empId, String leaveType, int noOfDays) {
        try {
            LeaveCounter counter = repo.findByEmpId(empId)
                    .orElseThrow(() -> new NoSuchElementException("No leave counter found for empId=" + empId));

            log.debug("Restoring {} days to {} leave for empId={}", noOfDays, leaveType, empId);

            switch (leaveType.trim().toUpperCase()) {
                case "CASUAL":
                    counter.setCasualLeaves(counter.getCasualLeaves() + noOfDays);
                    break;
                case "SICK":
                    counter.setSickLeaves(counter.getSickLeaves() + noOfDays);
                    break;
                case "ANNUAL":
                    counter.setAnnualLeaves(counter.getAnnualLeaves() + noOfDays);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid leave type: " + leaveType + ". Valid types: CASUAL, SICK, ANNUAL");
            }

            repo.save(counter);

            int newBalance = leaveType.equalsIgnoreCase("CASUAL") ? counter.getCasualLeaves() :
                    leaveType.equalsIgnoreCase("SICK") ? counter.getSickLeaves() :
                            (int) counter.getAnnualLeaves();

            log.info("Restored {} days of {} leave for empId={}. New balance: {}",
                    noOfDays, leaveType, empId, newBalance);

        } catch (NoSuchElementException | IllegalArgumentException e) {
            log.error("Error restoring leave for empId={}: {}", empId, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error restoring leave for empId={}: {}", empId, e.getMessage(), e);
            throw new RuntimeException("Failed to restore leave for empId=" + empId, e);
        }
    }
}
