package com.tcon.empManagement.Service;

import com.tcon.empManagement.Entity.Employee;
import com.tcon.empManagement.Entity.EmployeeHistory;

import java.time.Instant;
import java.util.List;

public interface EmployeeHistoryService {

    void recordCreate(Employee employee, String changedBy);

    void recordUpdate(Employee employee, String changedBy);

    void recordDelete(Employee employee, String changedBy);

    List<EmployeeHistory> getHistoryByEmployeeId(String employeeId);

    List<EmployeeHistory> getHistoryByEmpId(String empId);

    List<EmployeeHistory> getHistoryByOperation(String operation);

    List<EmployeeHistory> getHistoryByStatus(String status);

    List<EmployeeHistory> getHistoryByDateRange(Instant start, Instant end);

    List<EmployeeHistory> getHistoryByChangedBy(String changedBy);

    List<EmployeeHistory> getAllHistory();
}
