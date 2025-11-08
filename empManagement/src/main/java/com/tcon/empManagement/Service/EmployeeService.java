package com.tcon.empManagement.Service;


import com.tcon.empManagement.Dto.EmployeeCreateRequest;
import com.tcon.empManagement.Dto.EmployeeResponse;
import com.tcon.empManagement.Dto.EmployeeUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {
    EmployeeResponse create(EmployeeCreateRequest request);
    EmployeeResponse updateById(String id, EmployeeUpdateRequest request);
    EmployeeResponse getById(String id);
    EmployeeResponse getByEmpId(String empId);
    Page<EmployeeResponse> list(Pageable pageable);
    void deleteById(String id);
}

