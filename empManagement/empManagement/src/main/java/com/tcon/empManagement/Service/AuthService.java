package com.tcon.empManagement.Service;

import com.tcon.empManagement.Dto.LoginRequest;
import com.tcon.empManagement.Dto.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);

    LoginResponse logout(String empId);
}
