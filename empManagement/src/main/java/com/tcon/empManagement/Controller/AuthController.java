package com.tcon.empManagement.Controller;

import com.mongodb.DuplicateKeyException;
import com.tcon.empManagement.Dto.LoginRequest;
import com.tcon.empManagement.Dto.LoginResponse;
import com.tcon.empManagement.Dto.RegisterRequest;
import com.tcon.empManagement.Entity.AuthUser;
import com.tcon.empManagement.Service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {

            LoginResponse response = authService.login(request);
            return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            AuthUser user = authService.register(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
