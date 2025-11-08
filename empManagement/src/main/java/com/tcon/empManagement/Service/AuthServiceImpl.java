package com.tcon.empManagement.Service;

import com.tcon.empManagement.Dto.LoginRequest;
import com.tcon.empManagement.Dto.LoginResponse;
import com.tcon.empManagement.Dto.RegisterRequest;
import com.tcon.empManagement.Entity.AuthUser;
import com.tcon.empManagement.Repository.AuthUserRepository;
import com.tcon.empManagement.Security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        AuthUser user = authUserRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
        if (!user.isActive())
            throw new IllegalArgumentException("User account is inactive.");
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new IllegalArgumentException("Invalid email or password.");
        String token = jwtTokenProvider.generateToken(user.getEmail(), user.getEmpId(), user.getEmpRole());
        return LoginResponse.builder()
                .role(user.getEmpRole())
                .empId(user.getEmpId())
                .email(user.getEmail())
                .build();
    }

    @Override
    @Transactional
    public AuthUser register(RegisterRequest request) {
        if (authUserRepository.existsByEmail(request.getEmail()))
            throw new DuplicateKeyException("Email already exists: " + request.getEmail());
        if (authUserRepository.findByEmpId(request.getEmpId()).isPresent())
            throw new DuplicateKeyException("empId already exists: " + request.getEmpId());

        AuthUser user = AuthUser.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .empRole(request.getEmpRole())
                .empId(request.getEmpId())
                .isActive(request.isActive())
                .build();
        return authUserRepository.save(user);
    }
}
