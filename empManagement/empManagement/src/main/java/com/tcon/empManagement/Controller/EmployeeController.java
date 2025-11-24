package com.tcon.empManagement.Controller;

import com.tcon.empManagement.Dto.EmployeeCreateRequest;
import com.tcon.empManagement.Dto.EmployeeResponse;
import com.tcon.empManagement.Dto.EmployeeUpdateRequest;
import com.tcon.empManagement.Service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = "*")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // ============ CREATE ============
    @PostMapping
    public ResponseEntity<EmployeeResponse> createEmployee(
            @Valid @RequestBody EmployeeCreateRequest request,
            @RequestParam String createdBy) {
        log.info("POST /api/employees - createEmployee called, createdBy={}", createdBy);
        try {
            EmployeeResponse response = employeeService.createEmployee(request, createdBy);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception ex) {
            log.error("POST /api/employees failed: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ============ UPDATE ============
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponse> updateEmployee(
            @PathVariable String id,
            @RequestBody EmployeeUpdateRequest request,
            @RequestParam String updatedBy) {
        log.info("PUT /api/employees/{} - updateEmployee called, updatedBy={}", id, updatedBy);
        try {
            EmployeeResponse response = employeeService.updateEmployee(id, request, updatedBy);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            log.error("PUT /api/employees/{} failed: {}", id, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ============ DELETE ============
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(
            @PathVariable String id,
            @RequestParam String deletedBy) {
        log.info("DELETE /api/employees/{} - deleteEmployee called, deletedBy={}", id, deletedBy);
        try {
            employeeService.deleteEmployee(id, deletedBy);
            return ResponseEntity.noContent().build();
        } catch (Exception ex) {
            log.error("DELETE /api/employees/{} failed: {}", id, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ============ GET BY ID ============
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> getEmployeeById(@PathVariable String id) {
        log.info("GET /api/employees/{} - getEmployeeById called", id);
        try {
            EmployeeResponse response = employeeService.getEmployeeById(id);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            log.error("GET /api/employees/{} failed: {}", id, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // ============ GET BY EMP ID ============
    @GetMapping("/empid/{empId}")
    public ResponseEntity<EmployeeResponse> getEmployeeByEmpId(@PathVariable String empId) {
        log.info("GET /api/employees/empid/{} - getEmployeeByEmpId called", empId);
        try {
            EmployeeResponse response = employeeService.getEmployeeByEmpId(empId);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            log.error("GET /api/employees/empid/{} failed: {}", empId, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // ============ GET BY EMAIL ============
    @GetMapping("/email/{email}")
    public ResponseEntity<EmployeeResponse> getEmployeeByEmail(@PathVariable String email) {
        log.info("GET /api/employees/email/{} - getEmployeeByEmail called", email);
        try {
            EmployeeResponse response = employeeService.getEmployeeByEmail(email);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            log.error("GET /api/employees/email/{} failed: {}", email, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // ============ GET ALL ============
    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees() {
        log.info("GET /api/employees - getAllEmployees called");
        try {
            List<EmployeeResponse> responses = employeeService.getAllEmployees();
            return ResponseEntity.ok(responses);
        } catch (Exception ex) {
            log.error("GET /api/employees failed: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ============ GET BY STATUS ============
    @GetMapping("/active")
    public ResponseEntity<List<EmployeeResponse>> getActiveEmployees() {
        log.info("GET /api/employees/active - getActiveEmployees called");
        try {
            List<EmployeeResponse> responses = employeeService.getActiveEmployees();
            return ResponseEntity.ok(responses);
        } catch (Exception ex) {
            log.error("GET /api/employees/active failed: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/inactive")
    public ResponseEntity<List<EmployeeResponse>> getInactiveEmployees() {
        log.info("GET /api/employees/inactive - getInactiveEmployees called");
        try {
            List<EmployeeResponse> responses = employeeService.getInactiveEmployees();
            return ResponseEntity.ok(responses);
        } catch (Exception ex) {
            log.error("GET /api/employees/inactive failed: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ============ GET BY ROLE ============
    @GetMapping("/role/{empRole}")
    public ResponseEntity<List<EmployeeResponse>> getEmployeesByRole(@PathVariable String empRole) {
        log.info("GET /api/employees/role/{} - getEmployeesByRole called", empRole);
        try {
            List<EmployeeResponse> responses = employeeService.getEmployeesByRole(empRole);
            return ResponseEntity.ok(responses);
        } catch (Exception ex) {
            log.error("GET /api/employees/role/{} failed: {}", empRole, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ============ ACTIVATE / DEACTIVATE ============
    @PutMapping("/{id}/activate")
    public ResponseEntity<EmployeeResponse> activateEmployee(
            @PathVariable String id,
            @RequestParam String activatedBy) {
        log.info("PUT /api/employees/{}/activate - activateEmployee called, activatedBy={}", id, activatedBy);
        try {
            EmployeeResponse response = employeeService.activateEmployee(id, activatedBy);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            log.error("PUT /api/employees/{}/activate failed: {}", id, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<EmployeeResponse> deactivateEmployee(
            @PathVariable String id,
            @RequestParam String deactivatedBy) {
        log.info("PUT /api/employees/{}/deactivate - deactivateEmployee called, deactivatedBy={}", id, deactivatedBy);
        try {
            EmployeeResponse response = employeeService.deactivateEmployee(id, deactivatedBy);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            log.error("PUT /api/employees/{}/deactivate failed: {}", id, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/empid/{empId}/profile-image")
    public ResponseEntity<EmployeeResponse> uploadProfileImage(
            @PathVariable String empId,
            @RequestParam("file") MultipartFile file) {
        log.info("Uploading profile image for empId={}", empId);
        try {
            EmployeeResponse response = employeeService.uploadProfileImage(empId, file);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            log.error("Failed to upload profile image for empId={}: {}", empId, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/empid/{empId}/profile-image")
    public ResponseEntity<byte[]> getProfileImage(@PathVariable String empId) {
        log.info("Fetching profile image for empId={}", empId);
        try {
            return employeeService.getProfileImage(empId);
        } catch (Exception ex) {
            log.error("Failed to fetch profile image for empId={}: {}", empId, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    // NEW: Delete Profile Image Endpoint
    @DeleteMapping("/empid/{empId}/profile-image")
    public ResponseEntity<Void> deleteProfileImage(@PathVariable String empId) {
        log.info("DELETE /api/employees/empid/{}/profile-image - deleteProfileImage called", empId);
        try {
            employeeService.deleteProfileImage(empId);
            log.info("Profile image deleted successfully for empId={}", empId);
            return ResponseEntity.noContent().build();
        } catch (Exception ex) {
            log.error("DELETE /api/employees/empid/{}/profile-image failed: {}", empId, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
  }
}


}
