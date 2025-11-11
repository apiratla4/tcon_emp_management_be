package com.tcon.empManagement.Service;

import com.tcon.empManagement.Dto.LoginRequest;
import com.tcon.empManagement.Dto.LoginResponse;
import com.tcon.empManagement.Entity.Employee;
import com.tcon.empManagement.Repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public LoginResponse login(LoginRequest request) {
        log.info("Login attempt for empId: {}", request.getEmpId());

        try {
            // Find employee by empId
            Optional<Employee> employeeOptional = employeeRepository.findByEmpId(request.getEmpId());

            if (employeeOptional.isEmpty()) {
                log.warn("Login failed: Employee not found with empId: {}", request.getEmpId());
                return LoginResponse.builder()
                        .success(false)
                        .message("Invalid employee ID or password")
                        .build();
            }

            Employee employee = employeeOptional.get();

            // Check if employee is active
            if ("INACTIVE".equals(employee.getStatus())) {
                log.warn("Login failed: Employee account is inactive. empId: {}", request.getEmpId());
                return LoginResponse.builder()
                        .success(false)
                        .message("Your account is inactive. Please contact HR.")
                        .build();
            }

            // Verify password
            if (!passwordEncoder.matches(request.getPassword(), employee.getPassword())) {
                log.warn("Login failed: Invalid password for empId: {}", request.getEmpId());
                return LoginResponse.builder()
                        .success(false)
                        .message("Invalid employee ID or password")
                        .build();
            }

            // Successful login
            log.info("Login successful for empId: {}", request.getEmpId());
            return LoginResponse.builder()
                    .success(true)
                    .id(employee.getId())
                    .empId(employee.getEmpId())
                    .email(employee.getEmail())
                    .firstName(employee.getFirstName())
                    .lastName(employee.getLastName())
                    .empRole(employee.getEmpRole())
                    .status(employee.getStatus())
                    .message("Login successful")
                    .build();

        } catch (Exception e) {
            log.error("Error during login for empId: {}. Error: {}", request.getEmpId(), e.getMessage(), e);
            return LoginResponse.builder()
                    .success(false)
                    .message("An error occurred during login. Please try again.")
                    .build();
        }
    }

    @Override
    public LoginResponse logout(String empId) {
        log.info("Logout request for empId: {}", empId);

        try {
            Optional<Employee> employeeOptional = employeeRepository.findByEmpId(empId);

            if (employeeOptional.isEmpty()) {
                log.warn("Logout failed: Employee not found with empId: {}", empId);
                return LoginResponse.builder()
                        .success(false)
                        .message("Employee not found")
                        .build();
            }

            log.info("Logout successful for empId: {}", empId);
            return LoginResponse.builder()
                    .success(true)
                    .message("Logout successful")
                    .build();

        } catch (Exception e) {
            log.error("Error during logout for empId: {}. Error: {}", empId, e.getMessage(), e);
            return LoginResponse.builder()
                    .success(false)
                    .message("An error occurred during logout. Please try again.")
                    .build();
        }
    }
}
