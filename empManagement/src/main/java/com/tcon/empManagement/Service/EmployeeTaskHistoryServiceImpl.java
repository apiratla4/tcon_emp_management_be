package com.tcon.empManagement.Service;


import com.tcon.empManagement.Dto.EmployeeTaskHistoryCreateRequest;
import com.tcon.empManagement.Dto.EmployeeTaskHistoryResponse;
import com.tcon.empManagement.Dto.EmployeeTaskHistoryUpdateRequest;
import com.tcon.empManagement.Entity.EmployeeTaskHistory;
import com.tcon.empManagement.Repository.EmployeeTaskHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeTaskHistoryServiceImpl implements EmployeeTaskHistoryService {

    private final EmployeeTaskHistoryRepository repo;

    @Override
    public EmployeeTaskHistoryResponse create(EmployeeTaskHistoryCreateRequest request) {
        log.info("Creating task for empId={} name={}", request.getEmpId(), request.getEmpName());
        try {
            String status = (request.getStatus() == null || request.getStatus().isBlank())
                    ? "ASSIGNED" : request.getStatus();
            Instant now = Instant.now();
            Instant createdTime = request.getCreatedAtDateTime() != null ? request.getCreatedAtDateTime() : now;

            EmployeeTaskHistory entity = EmployeeTaskHistory.builder()
                    .empId(request.getEmpId())
                    .empName(request.getEmpName())
                    .taskName(request.getTaskName())
                    .taskDescription(request.getTaskDescription())
                    .status(status)
                    .dueDate(request.getDueDate())
                    .createdAtDateTime(createdTime)
                    .taskAssignedBy(request.getTaskAssignedBy())
                    .build();

            EmployeeTaskHistory saved = repo.save(entity);
            log.info("Task created id={}", saved.getId());
            return mapToResponse(saved);
        } catch (DuplicateKeyException ex) {
            log.error("Create task duplicate key empId={}", request.getEmpId(), ex);
            throw ex;
        } catch (Exception ex) {
            log.error("Create task failed empId={}", request.getEmpId(), ex);
            throw ex;
        }
    }

    @Override
    public EmployeeTaskHistoryResponse updateById(String id, EmployeeTaskHistoryUpdateRequest request) {
        log.info("Updating task id={}", id);
        try {
            EmployeeTaskHistory existing = repo.findById(id).orElseThrow(() -> {
                log.warn("Task not found id={}", id);
                return new NoSuchElementException("Task not found: " + id);
            });

            if (request.getTaskName() != null) existing.setTaskName(request.getTaskName());
            if (request.getTaskDescription() != null) existing.setTaskDescription(request.getTaskDescription());
            if (request.getDueDate() != null) existing.setDueDate(request.getDueDate());

            if (request.getStatus() != null) {
                String old = existing.getStatus();
                String nowStatus = request.getStatus();
                existing.setStatus(nowStatus);

                // Lifecycle timestamps auto-fill if not supplied
                Instant now = Instant.now();
                if ("PENDING".equalsIgnoreCase(nowStatus) && existing.getUpdatedAtDateTime() == null
                        && request.getUpdatedAtDateTime() == null) {
                    existing.setUpdatedAtDateTime(now);
                }
                if ("COMPLETED".equalsIgnoreCase(nowStatus) && existing.getCompletedAtDateTime() == null
                        && request.getCompletedAtDateTime() == null) {
                    existing.setCompletedAtDateTime(now);
                }
                log.info("Status change {} -> {} for task id={}", old, nowStatus, id);
            }

            if (request.getUpdatedAtDateTime() != null) {
                existing.setUpdatedAtDateTime(request.getUpdatedAtDateTime());
            }
            if (request.getCompletedAtDateTime() != null) {
                existing.setCompletedAtDateTime(request.getCompletedAtDateTime());
            }

            EmployeeTaskHistory saved = repo.save(existing);
            log.info("Task updated id={}", saved.getId());
            return mapToResponse(saved);
        } catch (Exception ex) {
            log.error("Update task failed id={}", id, ex);
            throw ex;
        }
    }

    @Override
    public EmployeeTaskHistoryResponse getById(String id) {
        log.info("Fetching task id={}", id);
        return repo.findById(id).map(task -> {
            log.info("Fetched task id={}", id);
            return mapToResponse(task);
        }).orElseThrow(() -> {
            log.warn("Task not found id={}", id);
            return new NoSuchElementException("Task not found: " + id);
        });
    }

    @Override
    public Page<EmployeeTaskHistoryResponse> list(Pageable pageable) {
        log.info("Listing tasks page={} size={}", pageable.getPageNumber(), pageable.getPageSize());
        return repo.findAll(pageable).map(this::mapToResponse);
    }

    @Override
    public List<EmployeeTaskHistoryResponse> listByEmpId(String empId) {
        log.info("Listing tasks by empId={}", empId);
        return repo.findByEmpIdOrderByCreatedAtDesc(empId).stream().map(this::mapToResponse).toList();
    }

    @Override
    public List<EmployeeTaskHistoryResponse> listByStatus(String status) {
        log.info("Listing tasks by status={}", status);
        return repo.findByStatusOrderByCreatedAtDesc(status).stream().map(this::mapToResponse).toList();
    }

    @Override
    public void deleteById(String id) {
        log.info("Deleting task id={}", id);
        if (!repo.existsById(id)) {
            log.warn("Delete failed, task not found id={}", id);
            throw new NoSuchElementException("Task not found: " + id);
        }
        repo.deleteById(id);
        log.info("Task deleted id={}", id);
    }

    private EmployeeTaskHistoryResponse mapToResponse(EmployeeTaskHistory e) {
        return EmployeeTaskHistoryResponse.builder()
                .id(e.getId())
                .empId(e.getEmpId())
                .empName(e.getEmpName())
                .taskName(e.getTaskName())
                .taskDescription(e.getTaskDescription())
                .status(e.getStatus())
                .dueDate(e.getDueDate())
                .createdAtDateTime(e.getCreatedAtDateTime())
                .updatedAtDateTime(e.getUpdatedAtDateTime())
                .completedAtDateTime(e.getCompletedAtDateTime())
                .taskAssignedBy(e.getTaskAssignedBy())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .build();
    }
}
