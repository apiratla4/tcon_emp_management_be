package com.tcon.empManagement.Service;

import com.tcon.empManagement.Dto.StoryTableCreateRequest;
import com.tcon.empManagement.Dto.StoryTableResponse;
import com.tcon.empManagement.Dto.StoryTableUpdateRequest;

import java.util.List;

public interface StoryTableService {

    StoryTableResponse createStoryTable(StoryTableCreateRequest request);

    StoryTableResponse updateStoryTable(String id, StoryTableUpdateRequest request);

    StoryTableResponse getStoryTableById(String id);

    List<StoryTableResponse> getAllStoryTables();

    List<StoryTableResponse> getStoryTablesByAssignedTo(String assignedTo);

    List<StoryTableResponse> getStoryTablesByDepartment(String department);

    List<StoryTableResponse> getStoryTablesByPriority(String priority);

    List<StoryTableResponse> getStoryTablesByStatus(String status);

    List<StoryTableResponse> getStoryTablesByProject(String project);

    List<StoryTableResponse> getStoryTablesByTaskName(String taskName);

    List<StoryTableResponse> getStoryTablesByType(String type);

    void deleteStoryTable(String id);
}
