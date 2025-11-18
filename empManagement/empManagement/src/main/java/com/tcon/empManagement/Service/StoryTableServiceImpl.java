package com.tcon.empManagement.Service;

import com.tcon.empManagement.Dto.*;
import com.tcon.empManagement.Entity.StoryTable;
import com.tcon.empManagement.Repository.StoryTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StoryTableServiceImpl implements StoryTableService {

    @Autowired
    private StoryTableRepository storyTableRepository;

    @Override
    public StoryTableResponse createStoryTable(StoryTableCreateRequest request) {
        StoryTable storyTable = new StoryTable();
        storyTable.setTaskName(request.getTaskName());
        storyTable.setTaskDescription(request.getTaskDescription());
        storyTable.setType(request.getType());
        storyTable.setDescription(request.getDescription());
        storyTable.setAssignedTo(request.getAssignedTo() != null ? request.getAssignedTo() : "unassigned");
        storyTable.setProject(request.getProject());
        storyTable.setDepartment(request.getDepartment());
        storyTable.setPriority(request.getPriority());
        storyTable.setStatus(request.getStatus() != null ? request.getStatus() : "PENDING");
        storyTable.setCreatedAt(LocalDateTime.now());
        storyTable.setUpdatedAt(LocalDateTime.now());
        storyTable.setSprintNumber(request.getSprintNumber());
        storyTable.setPreviousSprints(new ArrayList<>());
        storyTable.setSpillover(false);
        storyTable.setSpilloverFromSprint(null);
        storyTable.setEmpId(request.getEmpId());
        storyTable.setDueDate(request.getDueDate());

        StoryTable saved = storyTableRepository.save(storyTable);
        return convertToResponse(saved);
    }

    @Override
    public StoryTableResponse updateStoryTable(String id, StoryTableUpdateRequest request) {
        StoryTable storyTable = storyTableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("StoryTable not found with id: " + id));

        if (request.getSprintNumber() != null)
            storyTable.setSprintNumber(request.getSprintNumber());

        if (request.getPreviousSprints() != null)
            storyTable.setPreviousSprints(request.getPreviousSprints());

        if (request.getSpillover() != null)
            storyTable.setSpillover(request.getSpillover());

        if (request.getSpilloverFromSprint() != null)
            storyTable.setSpilloverFromSprint(request.getSpilloverFromSprint());

        if (request.getEmpId() != null)
            storyTable.setEmpId(request.getEmpId());

        if (request.getDueDate() != null)
            storyTable.setDueDate(request.getDueDate());


        if (request.getTaskName() != null) {
            storyTable.setTaskName(request.getTaskName());
        }
        if (request.getTaskDescription() != null) {
            storyTable.setTaskDescription(request.getTaskDescription());
        }
        if (request.getType() != null) {
            storyTable.setType(request.getType());
        }
        if (request.getDescription() != null) {
            storyTable.setDescription(request.getDescription());
        }
        if (request.getAssignedTo() != null) {
            storyTable.setAssignedTo(request.getAssignedTo());
        }
        if (request.getProject() != null) {
            storyTable.setProject(request.getProject());
        }
        if (request.getDepartment() != null) {
            storyTable.setDepartment(request.getDepartment());
        }
        if (request.getPriority() != null) {
            storyTable.setPriority(request.getPriority());
        }
        if (request.getStatus() != null) {
            storyTable.setStatus(request.getStatus());
        }

        storyTable.setUpdatedAt(LocalDateTime.now());

        StoryTable updated = storyTableRepository.save(storyTable);
        return convertToResponse(updated);
    }

    @Override
    public StoryTableResponse getStoryTableById(String id) {
        StoryTable storyTable = storyTableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("StoryTable not found with id: " + id));
        return convertToResponse(storyTable);
    }

    @Override
    public List<StoryTableResponse> getAllStoryTables() {
        return storyTableRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<StoryTableResponse> getStoryTablesByAssignedTo(String assignedTo) {
        return storyTableRepository.findByAssignedTo(assignedTo).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<StoryTableResponse> getStoryTablesByDepartment(String department) {
        return storyTableRepository.findByDepartment(department).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<StoryTableResponse> getStoryTablesByPriority(String priority) {
        return storyTableRepository.findByPriority(priority).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<StoryTableResponse> getStoryTablesByStatus(String status) {
        return storyTableRepository.findByStatus(status).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<StoryTableResponse> getStoryTablesByProject(String project) {
        return storyTableRepository.findByProject(project).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<StoryTableResponse> getStoryTablesByTaskName(String taskName) {
        return storyTableRepository.findByTaskName(taskName).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<StoryTableResponse> getStoryTablesByType(String type) {
        return storyTableRepository.findByType(type).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteStoryTable(String id) {
        if (!storyTableRepository.existsById(id)) {
            throw new RuntimeException("StoryTable not found with id: " + id);
        }
        storyTableRepository.deleteById(id);
    }

    private StoryTableResponse convertToResponse(StoryTable storyTable) {
        StoryTableResponse response = new StoryTableResponse();
        response.setId(storyTable.getId());
        response.setTaskName(storyTable.getTaskName());
        response.setTaskDescription(storyTable.getTaskDescription());
        response.setType(storyTable.getType());
        response.setDescription(storyTable.getDescription());
        response.setAssignedTo(storyTable.getAssignedTo());
        response.setProject(storyTable.getProject());
        response.setDepartment(storyTable.getDepartment());
        response.setPriority(storyTable.getPriority());
        response.setStatus(storyTable.getStatus());
        response.setCreatedAt(storyTable.getCreatedAt());
        response.setUpdatedAt(storyTable.getUpdatedAt());
        response.setSprintNumber(storyTable.getSprintNumber());
        response.setPreviousSprints(storyTable.getPreviousSprints());
        response.setSpillover(storyTable.getSpillover());
        response.setSpilloverFromSprint(storyTable.getSpilloverFromSprint());
        response.setEmpId(storyTable.getEmpId());
        response.setDueDate(storyTable.getDueDate());

        return response;
    }
}
