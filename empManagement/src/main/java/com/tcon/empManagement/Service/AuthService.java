package com.tcon.empManagement.Service;

import com.tcon.empManagement.Dto.LoginRequest;
import com.tcon.empManagement.Dto.LoginResponse;
import com.tcon.empManagement.Dto.RegisterRequest;
import com.tcon.empManagement.Entity.AuthUser;

public interface AuthService {
    LoginResponse login(LoginRequest request);
    AuthUser register(RegisterRequest request);
}
