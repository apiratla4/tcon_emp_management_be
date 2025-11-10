package com.tcon.empManagement.Service;

import com.tcon.empManagement.Entity.Employee;
import com.tcon.empManagement.Entity.EmployeeHistory;
import com.tcon.empManagement.Repository.EmployeeHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class EmployeeHistoryServiceImpl implements EmployeeHistoryService {

    @Autowired
    private EmployeeHistoryRepository employeeHistoryRepository;

    @Override
    public void recordCreate(Employee employee, String changedBy) {
        Instant now = Instant.now();
        EmployeeHistory history = mapEmployeeToHistory(employee);
        history.setEmployeeId(employee.getId());
        history.setOperation("CREATE");
        history.setChangedBy(changedBy);
        history.setOperationTime(now);
        history.setCreatedTime(now);

        employeeHistoryRepository.save(history);
    }

    @Override
    public void recordUpdate(Employee employee, String changedBy) {
        Instant now = Instant.now();
        EmployeeHistory history = mapEmployeeToHistory(employee);
        history.setEmployeeId(employee.getId());
        history.setOperation("UPDATE");
        history.setChangedBy(changedBy);
        history.setOperationTime(now);
        history.setUpdatedTime(now);

        employeeHistoryRepository.save(history);
    }

    @Override
    public void recordDelete(Employee employee, String changedBy) {
        Instant now = Instant.now();
        EmployeeHistory history = mapEmployeeToHistory(employee);
        history.setEmployeeId(employee.getId());
        history.setOperation("DELETE");
        history.setChangedBy(changedBy);
        history.setOperationTime(now);
        history.setDeletedTime(now);

        employeeHistoryRepository.save(history);
    }

    @Override
    public List<EmployeeHistory> getHistoryByEmployeeId(String employeeId) {
        return employeeHistoryRepository.findByEmployeeIdOrderByOperationTimeDesc(employeeId);
    }

    @Override
    public List<EmployeeHistory> getHistoryByEmpId(String empId) {
        return employeeHistoryRepository.findByEmpId(empId);
    }

    @Override
    public List<EmployeeHistory> getHistoryByOperation(String operation) {
        return employeeHistoryRepository.findByOperation(operation);
    }

    @Override
    public List<EmployeeHistory> getHistoryByStatus(String status) {
        return employeeHistoryRepository.findByStatus(status);
    }

    @Override
    public List<EmployeeHistory> getHistoryByDateRange(Instant start, Instant end) {
        return employeeHistoryRepository.findByOperationTimeBetween(start, end);
    }

    @Override
    public List<EmployeeHistory> getHistoryByChangedBy(String changedBy) {
        return employeeHistoryRepository.findByChangedBy(changedBy);
    }

    @Override
    public List<EmployeeHistory> getAllHistory() {
        return employeeHistoryRepository.findAll();
    }

    // Helper method to map Employee to EmployeeHistory
    private EmployeeHistory mapEmployeeToHistory(Employee employee) {
        return EmployeeHistory.builder()
                .id(employee.getId())
                .title(employee.getTitle())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .password(employee.getPassword())
                .phoneNumber(employee.getPhoneNumber())
                .empRole(employee.getEmpRole())
                .empId(employee.getEmpId())
                .bloodGroup(employee.getBloodGroup())
                .salary(employee.getSalary())
                .status(employee.getStatus())
                .address(mapAddress(employee.getAddress()))
                .bankDetails(mapBankDetails(employee.getBankDetails()))
                .emergencyContact(mapEmergencyContact(employee.getEmergencyContact()))
                .createdAt(employee.getCreatedAt())
                .updatedAt(employee.getUpdatedAt())
                .build();
    }

    private EmployeeHistory.Address mapAddress(Employee.Address address) {
        if (address == null) return null;
        return EmployeeHistory.Address.builder()
                .address1(address.getAddress1())
                .address2(address.getAddress2())
                .country(address.getCountry())
                .city(address.getCity())
                .pincode(address.getPincode())
                .build();
    }

    private EmployeeHistory.BankDetails mapBankDetails(Employee.BankDetails bankDetails) {
        if (bankDetails == null) return null;
        return EmployeeHistory.BankDetails.builder()
                .bankAccount(bankDetails.getBankAccount())
                .ifscCode(bankDetails.getIfscCode())
                .bankName(bankDetails.getBankName())
                .branchName(bankDetails.getBranchName())
                .build();
    }

    private EmployeeHistory.EmergencyContact mapEmergencyContact(Employee.EmergencyContact emergencyContact) {
        if (emergencyContact == null) return null;
        return EmployeeHistory.EmergencyContact.builder()
                .name(emergencyContact.getName())
                .contactNumber(emergencyContact.getContactNumber())
                .relation(emergencyContact.getRelation())
                .build();
    }
}
