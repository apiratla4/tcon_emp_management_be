package com.tcon.empManagement.Service;

import com.tcon.empManagement.Dto.LeaveCounterResponse;
import com.tcon.empManagement.Dto.LeaveCounterUpdateRequest;
import com.tcon.empManagement.Entity.Employee;
import com.tcon.empManagement.Entity.LeaveApprovel;
import com.tcon.empManagement.Entity.LeaveCounter;
import com.tcon.empManagement.Repository.EmployeeRepository;
import com.tcon.empManagement.Repository.LeaveApprovelRepository;
import com.tcon.empManagement.Repository.LeaveCounterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class LeaveCounterServiceImpl implements LeaveCounterService {

    private final LeaveCounterRepository repo;
    private final LeaveApprovelRepository leaveApprovelRepo;
    private final EmployeeRepository employeeRepository;

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

    @Override
    public LeaveCounterResponse initializeCounter(String empId) {
        try {
            if (repo.findByEmpId(empId).isPresent()) {
                log.warn("Leave counter already exists for empId={}. Skipping initialization.", empId);
                throw new IllegalStateException("Leave counter already exists for empId=" + empId);
            }

            // ✅ Get employee joining date
            Employee employee = employeeRepository.findByEmpId(empId)
                    .orElseThrow(() -> new NoSuchElementException("Employee not found: " + empId));

            Instant joiningDate = employee.getJoiningDate();
            if (joiningDate == null) {
                joiningDate = Instant.now();
                log.warn("No joining date found for empId={}. Using current date.", empId);
            }

            // ✅ Calculate pro-rated annual leaves based on joining date
            double initialAnnualLeaves = calculateProRatedAnnualLeaves(joiningDate);

            LeaveCounter lc = LeaveCounter.builder()
                    .empId(empId)
                    .casualLeaves(2)
                    .sickLeaves(5)
                    .annualLeaves(initialAnnualLeaves)
                    .lossOfPayLeaves(0)
                    .publicHolidays(new ArrayList<>())
                    .build();

            LeaveCounter saved = repo.save(lc);
            log.info("Leave counter initialized for empId={} with joiningDate={}: Casual=2, Sick=5, Annual={}, LOP=0",
                    empId, joiningDate, initialAnnualLeaves);
            return mapResponse(saved);
        } catch (IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error initializing leave counter for empId={}: {}", empId, e.getMessage(), e);
            throw new RuntimeException("Failed to initialize leave counter for empId=" + empId, e);
        }
    }

    /**
     * ✅ Calculate pro-rated annual leaves based on joining date
     * Handles multi-year scenarios with carry forward
     */
    private double calculateProRatedAnnualLeaves(Instant joiningDate) {
        LocalDate joinDate = LocalDate.ofInstant(joiningDate, ZoneId.systemDefault());
        LocalDate today = LocalDate.now();

        int joinYear = joinDate.getYear();
        int currentYear = today.getYear();

        double totalAccrued = 0.0;

        if (joinYear == currentYear) {
            // ✅ Scenario 1: Joined this year - calculate from joining date
            long monthsWorked = java.time.temporal.ChronoUnit.MONTHS.between(joinDate, today);
            int dayOfMonth = today.getDayOfMonth();
            int daysInMonth = today.lengthOfMonth();
            double partialMonth = (double) dayOfMonth / daysInMonth;

            totalAccrued = (monthsWorked + partialMonth) * 0.75;
            totalAccrued = Math.min(totalAccrued, 9.0);

            log.debug("First year accrual for empId joining {}: monthsWorked={}, partialMonth={}, accrued={}",
                    joinDate, monthsWorked, partialMonth, totalAccrued);

        } else {
            // ✅ Scenario 2: Joined in previous year(s) - calculate carry forward + current year accrual

            // Calculate what should have been accrued in first year
            LocalDate endOfJoinYear = LocalDate.of(joinYear, 12, 31);
            long monthsInFirstYear = java.time.temporal.ChronoUnit.MONTHS.between(joinDate, endOfJoinYear);

            double firstYearAccrued = (monthsInFirstYear + 1) * 0.75;
            firstYearAccrued = Math.min(firstYearAccrued, 9.0);

            // Assume maximum carry forward of 5 days
            double carriedForward = Math.min(firstYearAccrued, 5.0);

            log.debug("Previous year accrual: firstYearAccrued={}, carriedForward={}",
                    firstYearAccrued, carriedForward);

            // Calculate current year accrual (Jan 1 to today)
            LocalDate startOfCurrentYear = LocalDate.of(currentYear, 1, 1);
            long monthsThisYear = java.time.temporal.ChronoUnit.MONTHS.between(startOfCurrentYear, today);
            int dayOfMonth = today.getDayOfMonth();
            int daysInMonth = today.lengthOfMonth();
            double partialMonth = (double) dayOfMonth / daysInMonth;

            double currentYearAccrued = (monthsThisYear + partialMonth) * 0.75;
            currentYearAccrued = Math.min(currentYearAccrued, 9.0);

            log.debug("Current year accrual: monthsThisYear={}, partialMonth={}, currentYearAccrued={}",
                    monthsThisYear, partialMonth, currentYearAccrued);

            // Total = carried forward + current year accrual
            totalAccrued = carriedForward + currentYearAccrued;
            totalAccrued = Math.min(totalAccrued, 14.0);

            log.info("Multi-year accrual for empId joining {}: carriedForward={}, currentYear={}, total={}",
                    joinDate, carriedForward, currentYearAccrued, totalAccrued);
        }

        return Math.round(totalAccrued * 100.0) / 100.0;
    }

    /**
     * ✅ Map entity to response DTO with dynamic totals
     */
    /**
     * ✅ Map entity to response DTO with dynamic totals
     */
    private LeaveCounterResponse mapResponse(LeaveCounter counter) {
        final int TOTAL_CASUAL = 2;
        final int TOTAL_SICK = 5;

        // ✅ Calculate dynamic annual total based on joining date + used leaves
        double totalAnnual = calculateDynamicAnnualTotalFromJoining(counter.getEmpId(), counter.getAnnualLeaves());

        // ✅ Log before building response
        log.info("📤 Mapping response for empId={}: casualLeaves={}, sickLeaves={}, annualLeaves={}, lopLeaves={}",
                counter.getEmpId(),
                counter.getCasualLeaves(), counter.getSickLeaves(), counter.getAnnualLeaves(), counter.getLossOfPayLeaves());

        log.info("📤 Setting totals: totalCasual={}, totalSick={}, totalAnnual={}",
                TOTAL_CASUAL, TOTAL_SICK, totalAnnual);

        // ✅ Build response object FIRST
        LeaveCounterResponse response = LeaveCounterResponse.builder()
                .empId(counter.getEmpId())
                .casualLeaves(counter.getCasualLeaves())
                .sickLeaves(counter.getSickLeaves())
                .annualLeaves(counter.getAnnualLeaves())
                .lossOfPayLeaves(counter.getLossOfPayLeaves())
                .totalCasualLeaves(TOTAL_CASUAL)
                .totalSickLeaves(TOTAL_SICK)
                .totalAnnualLeaves(totalAnnual)
                .publicHolidays(counter.getPublicHolidays() != null ?
                        counter.getPublicHolidays() : Collections.emptyList())
                .build();

        // ✅ NOW log the response (after it's built)
        log.info("✅ Response built with totals: totalCasual={}, totalSick={}, totalAnnual={}",
                response.getTotalCasualLeaves(), response.getTotalSickLeaves(), response.getTotalAnnualLeaves());

        return response;
    }

    /**
     * ✅ Calculate total annual = accrued from joining + used leaves
     */
    private double calculateDynamicAnnualTotalFromJoining(String empId, double remainingAnnual) {
        try {
            Employee employee = employeeRepository.findByEmpId(empId).orElse(null);
            if (employee == null || employee.getJoiningDate() == null) {
                log.warn("No employee or joining date for empId={}. Using remaining balance.", empId);
                return remainingAnnual;
            }

            Instant joiningDate = employee.getJoiningDate();
            LocalDate joinDate = LocalDate.ofInstant(joiningDate, ZoneId.systemDefault());
            LocalDate today = LocalDate.now();
            int joinYear = joinDate.getYear();
            int currentYear = today.getYear();

            // Get all APPROVED annual leaves
            List<LeaveApprovel> approvedLeaves = leaveApprovelRepo.findByEmpIdOrderByCreateDateDesc(empId)
                    .stream()
                    .filter(leave -> "APPROVED".equalsIgnoreCase(leave.getStatus()))
                    .filter(leave -> {
                        String type = leave.getTypeOfLeave();
                        return type != null && (type.toUpperCase().contains("ANNUAL") ||
                                type.toUpperCase().contains("VACATION"));
                    })
                    .toList();

            // Calculate total used annual leaves (all time)
            double totalUsedAllTime = approvedLeaves.stream()
                    .mapToDouble(leave -> leave.getNoOfDays() != null ? leave.getNoOfDays() : 0.0)
                    .sum();

            double totalEverAccrued;

            if (joinYear == currentYear) {
                // ✅ Joined this year - simple calculation
                long monthsWorked = java.time.temporal.ChronoUnit.MONTHS.between(joinDate, today);
                int dayOfMonth = today.getDayOfMonth();
                int daysInMonth = today.lengthOfMonth();
                double partialMonth = (double) dayOfMonth / daysInMonth;

                totalEverAccrued = (monthsWorked + partialMonth) * 0.75;
                totalEverAccrued = Math.min(totalEverAccrued, 9.0);

            } else {
                // ✅ Joined in previous year - year-by-year calculation

                // First year accrual
                LocalDate endOfJoinYear = LocalDate.of(joinYear, 12, 31);
                long monthsInFirstYear = java.time.temporal.ChronoUnit.MONTHS.between(joinDate, endOfJoinYear);
                double firstYearAccrual = Math.min((monthsInFirstYear + 1) * 0.75, 9.0);

                // Get used leaves in first year
                double usedInFirstYear = approvedLeaves.stream()
                        .filter(leave -> {
                            LocalDate leaveDate = leave.getFromDate();
                            return leaveDate.getYear() == joinYear;
                        })
                        .mapToDouble(leave -> leave.getNoOfDays() != null ? leave.getNoOfDays() : 0.0)
                        .sum();

                double actualCarriedForward = Math.min(firstYearAccrual - usedInFirstYear, 5.0);
                actualCarriedForward = Math.max(actualCarriedForward, 0);

                // Current year accrual
                LocalDate startOfCurrentYear = LocalDate.of(currentYear, 1, 1);
                long monthsThisYear = java.time.temporal.ChronoUnit.MONTHS.between(startOfCurrentYear, today);
                int dayOfMonth = today.getDayOfMonth();
                int daysInMonth = today.lengthOfMonth();
                double partialMonth = (double) dayOfMonth / daysInMonth;

                double currentYearAccrual = Math.min((monthsThisYear + partialMonth) * 0.75, 9.0);

                totalEverAccrued = actualCarriedForward + currentYearAccrual;
                totalEverAccrued = Math.min(totalEverAccrued, 14.0);

                log.debug("Multi-year total for empId={}: firstYear={}, usedFirstYear={}, carried={}, currentYear={}, totalAccrued={}",
                        empId, firstYearAccrual, usedInFirstYear, actualCarriedForward, currentYearAccrual, totalEverAccrued);
            }

            // Total to display = remaining + used
            double total = remainingAnnual + totalUsedAllTime;
            total = Math.min(total, totalEverAccrued);

            log.debug("Dynamic total for empId={}: remaining={}, totalUsed={}, everAccrued={}, displayTotal={}",
                    empId, remainingAnnual, totalUsedAllTime, totalEverAccrued, total);

            return Math.round(total * 100.0) / 100.0;

        } catch (Exception e) {
            log.error("Error calculating dynamic annual total for empId={}: {}", empId, e.getMessage(), e);
            return remainingAnnual;
        }
    }

    @Override
    public void accrueAnnualLeaveMonthly() {
        try {
            List<LeaveCounter> allCounters = repo.findAll();
            int accruedCount = 0;

            for (LeaveCounter lc : allCounters) {
                try {
                    Employee employee = employeeRepository.findByEmpId(lc.getEmpId()).orElse(null);
                    if (employee == null || employee.getJoiningDate() == null) {
                        log.warn("Skipping accrual for empId={}: No employee or joining date found", lc.getEmpId());
                        continue;
                    }

                    LocalDate joinDate = LocalDate.ofInstant(employee.getJoiningDate(), ZoneId.systemDefault());
                    LocalDate today = LocalDate.now();

                    // Only accrue if at least 1 month has passed since joining
                    if (joinDate.plusMonths(1).isAfter(today)) {
                        log.debug("Skipping accrual for empId={}: Less than 1 month since joining", lc.getEmpId());
                        continue;
                    }

                    double newBalance = lc.getAnnualLeaves() + 0.75;

                    if (newBalance > 14.0) {
                        log.warn("Annual leave cap reached for empId={}: current={}, not accruing",
                                lc.getEmpId(), lc.getAnnualLeaves());
                        continue;
                    }

                    lc.setAnnualLeaves(newBalance);
                    accruedCount++;

                } catch (Exception e) {
                    log.error("Error accruing leave for empId={}: {}", lc.getEmpId(), e.getMessage());
                }
            }

            if (accruedCount > 0) {
                repo.saveAll(allCounters);
            }

            log.info("Monthly annual leave accrual completed: {} employees accrued 0.75 leaves", accruedCount);

        } catch (Exception e) {
            log.error("Error during monthly annual leave accrual: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to accrue annual leaves", e);
        }
    }

    @Override
    public void carryForwardAnnualLeaves() {
        try {
            List<LeaveCounter> allCounters = repo.findAll();
            int totalEmployees = allCounters.size();
            int employeesWithCarryForward = 0;

            for (LeaveCounter lc : allCounters) {
                double currentBalance = lc.getAnnualLeaves();

                double carryForward = Math.min(currentBalance, 5.0);
                lc.setAnnualLeaves(carryForward);

                if (currentBalance > 5.0) {
                    employeesWithCarryForward++;
                    log.info("Employee {} had {} annual leaves. Carried forward: {}, Forfeited: {}",
                            lc.getEmpId(), currentBalance, carryForward, currentBalance - 5.0);
                } else {
                    log.debug("Employee {} carried forward all {} leaves", lc.getEmpId(), carryForward);
                }
            }

            repo.saveAll(allCounters);
            log.info("Year-end carry forward completed. Total: {}, With forfeit: {}, Total carried: {}",
                    totalEmployees, employeesWithCarryForward,
                    allCounters.stream().mapToDouble(LeaveCounter::getAnnualLeaves).sum());

        } catch (Exception e) {
            log.error("Error during year-end carry forward: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to carry forward annual leaves", e);
        }
    }
}
