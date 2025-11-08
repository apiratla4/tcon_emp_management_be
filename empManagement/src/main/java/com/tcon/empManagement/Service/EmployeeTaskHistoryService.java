package com.tcon.empManagement.Service;


import com.tcon.empManagement.Dto.EmployeeTaskHistoryCreateRequest;
import com.tcon.empManagement.Dto.EmployeeTaskHistoryResponse;
import com.tcon.empManagement.Dto.EmployeeTaskHistoryUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeTaskHistoryService {

    EmployeeTaskHistoryResponse create(EmployeeTaskHistoryCreateRequest request);

    EmployeeTaskHistoryResponse updateById(String id, EmployeeTaskHistoryUpdateRequest request);

    EmployeeTaskHistoryResponse getById(String id);

    Page<EmployeeTaskHistoryResponse> list(Pageable pageable);

    List<EmployeeTaskHistoryResponse> listByEmpId(String empId);

    List<EmployeeTaskHistoryResponse> listByStatus(String status);

    void deleteById(String id);
}

