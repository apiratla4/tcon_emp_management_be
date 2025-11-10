package com.tcon.empManagement.Service;

import com.tcon.empManagement.Dto.EmployeeCreateRequest;
import com.tcon.empManagement.Dto.EmployeeResponse;
import com.tcon.empManagement.Dto.EmployeeUpdateRequest;

import java.util.List;

public interface EmployeeService {

    EmployeeResponse createEmployee(EmployeeCreateRequest request, String createdBy);

    EmployeeResponse updateEmployee(String id, EmployeeUpdateRequest request, String updatedBy);

    void deleteEmployee(String id, String deletedBy);

    EmployeeResponse getEmployeeById(String id);

    EmployeeResponse getEmployeeByEmpId(String empId);

    EmployeeResponse getEmployeeByEmail(String email);

    List<EmployeeResponse> getAllEmployees();

    List<EmployeeResponse> getActiveEmployees();

    List<EmployeeResponse> getInactiveEmployees();

    List<EmployeeResponse> getEmployeesByRole(String empRole);

    EmployeeResponse activateEmployee(String id, String activatedBy);

    EmployeeResponse deactivateEmployee(String id, String deactivatedBy);
}
