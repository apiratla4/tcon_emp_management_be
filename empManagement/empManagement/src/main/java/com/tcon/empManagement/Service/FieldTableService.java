package com.tcon.empManagement.Service;
import com.tcon.empManagement.Dto.FieldTableCreateRequest;
import com.tcon.empManagement.Dto.FieldTableResponse;
import com.tcon.empManagement.Dto.FieldTableUpdateRequest;

import java.util.List;

public interface FieldTableService {

    FieldTableResponse createFieldTable(FieldTableCreateRequest request);

    FieldTableResponse updateFieldTable(String id, FieldTableUpdateRequest request);

    FieldTableResponse getFieldTableById(String id);

    List<FieldTableResponse> getAllFieldTables();

    List<FieldTableResponse> getFieldTablesByTaskName(String taskName);

    List<FieldTableResponse> getFieldTablesByPriority(String priority);

    List<FieldTableResponse> getFieldTablesByType(String type);

    List<FieldTableResponse> getFieldTablesByDeptName(String deptName);

    List<FieldTableResponse> getFieldTablesByDeptNameAndPriority(String deptName, String priority);

    void deleteFieldTable(String id);
}
