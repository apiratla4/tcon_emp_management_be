package com.tcon.empManagement.Service;


import com.tcon.empManagement.Dto.LeaveApprovelCreateRequest;
import com.tcon.empManagement.Dto.LeaveApprovelResponse;
import com.tcon.empManagement.Dto.LeaveApprovelUpdateStatusRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface LeaveApprovelService {
    LeaveApprovelResponse applyLeave(LeaveApprovelCreateRequest req);
    LeaveApprovelResponse updateStatus(String id, LeaveApprovelUpdateStatusRequest req);
    List<LeaveApprovelResponse> getByEmpId(String empId);
    Page<LeaveApprovelResponse> getAll(Pageable pageable);
    List<LeaveApprovelResponse> getByStatus(String status);
    List<LeaveApprovelResponse> getByDateRange(LocalDate from, LocalDate to);
    boolean deleteLeave(String id);
    LeaveApprovelResponse getById(String id);
}
