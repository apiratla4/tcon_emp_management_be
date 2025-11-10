package com.tcon.empManagement.Service;


import com.tcon.empManagement.Entity.Employee;
import com.tcon.empManagement.Repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;


    @Override
    public UserDetails loadUserByUsername(String empId) throws UsernameNotFoundException {
        Employee employee = employeeRepository.findByEmpId(empId)
                .orElseThrow(() -> new UsernameNotFoundException("Employee not found: " + empId));
        System.out.println("User loaded for empId=" + empId + ", role=" + employee.getEmpRole());
        return User.builder()
                .username(employee.getEmpId())
                .password(employee.getPassword())
                .authorities(Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_" + employee.getEmpRole().toUpperCase())
                ))
                .build();
    }

}
