package com.tcon.empManagement.Service;


import com.tcon.empManagement.Dto.LeaveApprovelCreateRequest;
import com.tcon.empManagement.Dto.LeaveApprovelResponse;
import com.tcon.empManagement.Dto.LeaveApprovelUpdateStatusRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LeaveApprovelService {
    LeaveApprovelResponse applyLeave(LeaveApprovelCreateRequest req);
    LeaveApprovelResponse updateStatus(String id, LeaveApprovelUpdateStatusRequest req);
    List<LeaveApprovelResponse> getByEmpId(String empId);
    Page<LeaveApprovelResponse> getAll(Pageable pageable);
}
