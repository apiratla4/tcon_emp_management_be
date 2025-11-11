package com.tcon.empManagement.Controller;

import com.tcon.empManagement.Dto.LoginRequest;
import com.tcon.empManagement.Dto.LoginResponse;
import com.tcon.empManagement.Service.AuthService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@Slf4j
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("Received login request for empId: {}", request.getEmpId());

        try {
            LoginResponse response = authService.login(request);

            if (response.isSuccess()) {
                log.info("Login successful for empId: {}", request.getEmpId());
                return ResponseEntity.ok(response);
            } else {
                log.warn("Login failed for empId: {}. Reason: {}", request.getEmpId(), response.getMessage());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

        } catch (Exception e) {
            log.error("Unexpected error during login for empId: {}. Error: {}", request.getEmpId(), e.getMessage(), e);
            LoginResponse errorResponse = LoginResponse.builder()
                    .success(false)
                    .message("An unexpected error occurred. Please try again later.")
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<LoginResponse> logout(@RequestParam String empId) {
        log.info("Received logout request for empId: {}", empId);

        try {
            LoginResponse response = authService.logout(empId);

            if (response.isSuccess()) {
                log.info("Logout successful for empId: {}", empId);
                return ResponseEntity.ok(response);
            } else {
                log.warn("Logout failed for empId: {}. Reason: {}", empId, response.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

        } catch (Exception e) {
            log.error("Unexpected error during logout for empId: {}. Error: {}", empId, e.getMessage(), e);
            LoginResponse errorResponse = LoginResponse.builder()
                    .success(false)
                    .message("An unexpected error occurred. Please try again later.")
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
