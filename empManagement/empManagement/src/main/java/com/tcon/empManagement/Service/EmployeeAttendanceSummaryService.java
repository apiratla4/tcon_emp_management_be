package com.tcon.empManagement.Service;

import com.tcon.empManagement.Dto.EmployeeAttendanceSummaryResponse;
import com.tcon.empManagement.Entity.Attendance;
import com.tcon.empManagement.Entity.Employee;
import com.tcon.empManagement.Entity.LeaveCounter;
import com.tcon.empManagement.Repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeAttendanceSummaryService {

    private final EmployeeAttendanceSummaryRepository summaryRepo;
    private final AttendanceRepository attendanceRepo;
    private final LeaveApprovelRepository leaveApprovelRepo;
    private final LeaveCounterRepository leaveCounterRepo;
    private final EmployeeRepository employeeRepo;
    private final LeaveCounterService leaveCounterService;

    public EmployeeAttendanceSummaryResponse getSummary(String empId, int year, int month) {
        log.info("Building attendance summary for empId={}, year={}, month={}", empId, year, month);

        // ----- 1. FETCH EMPLOYEE DETAILS (dept, panNo, joiningDate) -----
        Employee employee = employeeRepo.findByEmpId(empId)
                .orElseThrow(() -> new RuntimeException("Employee not found: " + empId));

        // ----- 2. Compute working days for this month -----
        int workingDays = calculateWorkingDays(year, month);

        // ----- 3. Define month range -----
        YearMonth ym = YearMonth.of(year, month);
        LocalDate monthStart = ym.atDay(1);
        LocalDate monthEnd = ym.atEndOfMonth();

        // ----- 4. Attendance: noOfDaysPresents for this month -----
        List<Attendance> attendanceList = attendanceRepo.findByEmpIdOrderByDateDesc(empId);
        double dayPresent = 0.0;

        for (Attendance att : attendanceList) {
            LocalDate date = att.getDate();
            if (date == null) continue;
            if (date.isBefore(monthStart) || date.isAfter(monthEnd)) continue;

            // ✅ Skip OnLeave days for present calculation
            if ("OnLeave".equals(att.getStatus())) continue;

            if (att.getCheckIn() != null && att.getCheckOut() != null) {
                double hours = Duration.between(att.getCheckIn(), att.getCheckOut()).toMinutes() / 60.0;
                if (hours >= 9) {
                    dayPresent += 1.0; // Full day
                } else if (hours > 0) {
                    dayPresent += 0.5; // Half day
                }
            }
        }
        int noOfDaysPresents = (int) dayPresent;

        // ----- 5. Leaves: noOfLeavesTaken in this month -----
        int noOfLeavesTaken = leaveApprovelRepo.findByEmpIdOrderByCreateDateDesc(empId)
                .stream()
                .filter(l -> "APPROVED".equalsIgnoreCase(l.getStatus()))
                .mapToInt(l -> {
                    LocalDate from = l.getFromDate();
                    LocalDate to = l.getToDate();
                    if (from == null || to == null) return 0;

                    // ✅ Calculate overlap with this month (corrected logic)
                    LocalDate effFrom = from.isBefore(monthStart) ? monthStart : from;
                    LocalDate effTo = to.isAfter(monthEnd) ? monthEnd : to;
                    if (effFrom.isAfter(effTo)) return 0;

                    return (int) ChronoUnit.DAYS.between(effFrom, effTo) + 1;
                })
                .sum();


        // 6. Loss of pay days (month-specific calculation)
        int lossOfPayDays = calculateMonthlyLossOfPayDays(empId, monthStart, monthEnd);


        // ----- 7. Build response with ALL fields -----
        return EmployeeAttendanceSummaryResponse.builder()
                .empId(empId)
                .noOfDaysPresents(noOfDaysPresents)
                .noOfLeavesTaken(noOfLeavesTaken)
                .lossOfPayDays(lossOfPayDays)
                .year(year)
                .month(month)
                .workingDays(workingDays)
                .dept(employee.getDept())
                .panNo(employee.getPanNo())
                .joiningDate(employee.getJoiningDate())
                .build();
    }

    /**
     * Calculate working days (total days - weekends - weekday holidays) for a given month.
     */
    private int calculateWorkingDays(int year, int month) {
        YearMonth ym = YearMonth.of(year, month);
        int daysInMonth = ym.lengthOfMonth();

        int weekendCount = 0;
        for (int d = 1; d <= daysInMonth; d++) {
            LocalDate date = ym.atDay(d);
            DayOfWeek dow = date.getDayOfWeek();
            if (dow == DayOfWeek.SATURDAY || dow == DayOfWeek.SUNDAY) {
                weekendCount++;
            }
        }

        // ✅ Get public holidays from LeaveCounterService
        List<LocalDate> publicHolidays = leaveCounterService.getPublicHolidays()
                .stream()
                .map(LocalDate::parse) // assuming ISO yyyy-MM-dd format
                .collect(Collectors.toList());

        long holidayCount = publicHolidays.stream()
                .filter(h -> h.getYear() == year && h.getMonthValue() == month)
                .filter(h -> {
                    DayOfWeek dow = h.getDayOfWeek();
                    return dow != DayOfWeek.SATURDAY && dow != DayOfWeek.SUNDAY;
                })
                .count();

        int workingDays = daysInMonth - weekendCount - (int) holidayCount;
        log.info("Calculated working days year={}, month={} => {}", year, month, workingDays);
        return workingDays;
    }

    /**
     * Calculate LOP days for specific month: workingDays - presents - paid leaves
     */
    private int calculateMonthlyLossOfPayDays(String empId, LocalDate monthStart, LocalDate monthEnd) {
        // 1) Working weekdays in this month (no weekends)
        int workingDaysInRange = 0;
        for (LocalDate d = monthStart; !d.isAfter(monthEnd); d = d.plusDays(1)) {
            DayOfWeek dow = d.getDayOfWeek();
            if (dow != DayOfWeek.SATURDAY && dow != DayOfWeek.SUNDAY) {
                workingDaysInRange++;
            }
        }

        // 2) Present days in this month (full + half)
        List<Attendance> attendanceList = attendanceRepo.findByEmpIdOrderByDateDesc(empId);
        double presentCount = 0.0;
        for (Attendance att : attendanceList) {
            LocalDate date = att.getDate();
            if (date == null) continue;
            if (date.isBefore(monthStart) || date.isAfter(monthEnd)) continue;
            if ("OnLeave".equals(att.getStatus())) continue; // not present

            if (att.getCheckIn() != null && att.getCheckOut() != null) {
                double hours = Duration.between(att.getCheckIn(), att.getCheckOut()).toMinutes() / 60.0;
                if (hours >= 9)      presentCount += 1.0;
                else if (hours > 0) presentCount += 0.5;
            }
        }
        int presents = (int) presentCount;

        // 3) APPROVED leave days in this month (all types)
        int approvedLeaveDays = leaveApprovelRepo.findByEmpIdOrderByCreateDateDesc(empId)
                .stream()
                .filter(l -> "APPROVED".equalsIgnoreCase(l.getStatus()))
                .mapToInt(l -> {
                    LocalDate from = l.getFromDate();
                    LocalDate to   = l.getToDate();
                    if (from == null || to == null) return 0;
                    LocalDate effFrom = from.isBefore(monthStart) ? monthStart : from;
                    LocalDate effTo   = to.isAfter(monthEnd) ? monthEnd : to;
                    if (effFrom.isAfter(effTo)) return 0;
                    return (int) ChronoUnit.DAYS.between(effFrom, effTo) + 1;
                })
                .sum();

        // 4) Fetch current leave balances (remaining paid leaves)
        LeaveCounter counter = leaveCounterRepo.findByEmpId(empId)
                .orElseThrow(() -> new RuntimeException("No leave counter for empId=" + empId));

        int remainingPaid =
                counter.getCasualLeaves()
                        + counter.getSickLeaves()
                        + (int) counter.getAnnualLeaves(); // annual is double

        // Paid days used this month = max(0, min(approvedLeaveDays, remainingPaid + already-used-paid))
        // For simplicity assume: remainingPaid are still available; any extra in this month is unpaid.

        int unpaidFromExcessLeave = Math.max(0, approvedLeaveDays - remainingPaid);

        // 5) Pure absents = working days - (presents + approved leaves)
        int pureAbsents = Math.max(0, workingDaysInRange - (presents + approvedLeaveDays));

        int lop = unpaidFromExcessLeave + pureAbsents;

        log.info("Monthly LOP empId={}, workingDays={}, presents={}, approvedLeaves={}, " +
                        "remainingPaid={}, unpaidFromExcess={}, pureAbsents={}, lop={}",
                empId, workingDaysInRange, presents, approvedLeaveDays,
                remainingPaid, unpaidFromExcessLeave, pureAbsents, lop);

        return Math.max(0, lop);
    }

}
