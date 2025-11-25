package com.tcon.empManagement.Service;

import com.tcon.empManagement.Dto.LeaveCounterResponse;
import com.tcon.empManagement.Dto.LeaveCounterUpdateRequest;
import com.tcon.empManagement.Entity.LeaveCounter;

import java.util.List;

public interface LeaveCounterService {
    LeaveCounterResponse getByEmpId(String empId);
    LeaveCounterResponse updateLeaveBalance(String empId, LeaveCounterUpdateRequest req);
    void deductLeave(String empId, String leaveType, int noOfDays);
    List<LeaveCounterResponse> getAllLeaveBalances();
    void addPublicHoliday(String date);
    List<String> getPublicHolidays();

    // Optional utility methods
    void carryForwardAnnualLeaves();
    LeaveCounterResponse initializeCounter(String empId);
}
