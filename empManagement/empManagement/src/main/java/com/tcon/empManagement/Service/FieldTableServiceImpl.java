package com.tcon.empManagement.Service;

import com.tcon.empManagement.Dto.FieldTableCreateRequest;
import com.tcon.empManagement.Dto.FieldTableResponse;
import com.tcon.empManagement.Dto.FieldTableUpdateRequest;
import com.tcon.empManagement.Entity.FieldTable;
import com.tcon.empManagement.Repository.FieldTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FieldTableServiceImpl implements FieldTableService {

    @Autowired
    private FieldTableRepository fieldTableRepository;

    @Override
    public FieldTableResponse createFieldTable(FieldTableCreateRequest request) {
        FieldTable fieldTable = new FieldTable();
        fieldTable.setTaskName(request.getTaskName());
        fieldTable.setTaskDescription(request.getTaskDescription());
        fieldTable.setPriority(request.getPriority());
        fieldTable.setType(request.getType());
        fieldTable.setDeptName(request.getDeptName());
        fieldTable.setCreatedAt(LocalDateTime.now());
        fieldTable.setUpdatedAt(LocalDateTime.now());

        FieldTable saved = fieldTableRepository.save(fieldTable);
        return convertToResponse(saved);
    }

    @Override
    public FieldTableResponse updateFieldTable(String id, FieldTableUpdateRequest request) {
        FieldTable fieldTable = fieldTableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("FieldTable not found with id: " + id));

        if (request.getTaskName() != null) {
            fieldTable.setTaskName(request.getTaskName());
        }
        if (request.getTaskDescription() != null) {
            fieldTable.setTaskDescription(request.getTaskDescription());
        }
        if (request.getPriority() != null) {
            fieldTable.setPriority(request.getPriority());
        }
        if (request.getType() != null) {
            fieldTable.setType(request.getType());
        }
        if (request.getDeptName() != null) {
            fieldTable.setDeptName(request.getDeptName());
        }

        fieldTable.setUpdatedAt(LocalDateTime.now());

        FieldTable updated = fieldTableRepository.save(fieldTable);
        return convertToResponse(updated);
    }

    @Override
    public FieldTableResponse getFieldTableById(String id) {
        FieldTable fieldTable = fieldTableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("FieldTable not found with id: " + id));
        return convertToResponse(fieldTable);
    }

    @Override
    public List<FieldTableResponse> getAllFieldTables() {
        return fieldTableRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<FieldTableResponse> getFieldTablesByTaskName(String taskName) {
        return fieldTableRepository.findByTaskName(taskName).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<FieldTableResponse> getFieldTablesByPriority(String priority) {
        return fieldTableRepository.findByPriority(priority).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<FieldTableResponse> getFieldTablesByType(String type) {
        return fieldTableRepository.findByType(type).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<FieldTableResponse> getFieldTablesByDeptName(String deptName) {
        return fieldTableRepository.findByDeptName(deptName).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<FieldTableResponse> getFieldTablesByDeptNameAndPriority(String deptName, String priority) {
        return fieldTableRepository.findByDeptNameAndPriority(deptName, priority).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteFieldTable(String id) {
        if (!fieldTableRepository.existsById(id)) {
            throw new RuntimeException("FieldTable not found with id: " + id);
        }
        fieldTableRepository.deleteById(id);
    }

    private FieldTableResponse convertToResponse(FieldTable fieldTable) {
        FieldTableResponse response = new FieldTableResponse();
        response.setId(fieldTable.getId());
        response.setTaskName(fieldTable.getTaskName());
        response.setTaskDescription(fieldTable.getTaskDescription());
        response.setPriority(fieldTable.getPriority());
        response.setType(fieldTable.getType());
        response.setDeptName(fieldTable.getDeptName());
        response.setCreatedAt(fieldTable.getCreatedAt());
        response.setUpdatedAt(fieldTable.getUpdatedAt());
        return response;
    }
}
