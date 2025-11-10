package com.tcon.empManagement.config;

import com.tcon.empManagement.Dto.EmployeeCreateRequest;

import com.tcon.empManagement.Dto.LoginRequest;
import com.tcon.empManagement.Dto.LoginResponse;
import com.tcon.empManagement.Entity.Employee;
import com.tcon.empManagement.Repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(EmployeeCreateRequest req) {
        if (employeeRepository.existsByEmpId(req.getEmpId()))
            throw new IllegalArgumentException("empId already exists");
        if (employeeRepository.existsByEmail(req.getEmail()))
            throw new IllegalArgumentException("Email already exists");

        Employee employee = Employee.builder()
                .title(req.getTitle())
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .phoneNumber(req.getPhoneNumber())
                .empRole(req.getEmpRole().toUpperCase())
                .empId(req.getEmpId().toUpperCase())
                .build();
        employeeRepository.save(employee);
    }

    public LoginResponse login(LoginRequest req) {
        Employee emp = employeeRepository.findByEmpId(req.getEmpId().toUpperCase())
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));
        if (!passwordEncoder.matches(req.getPassword(), emp.getPassword()))
            throw new IllegalArgumentException("Invalid password");

        return new LoginResponse(emp.getId(), emp.getEmpId(), emp.getEmail(), emp.getEmpRole(), "Login successful");
    }
}
