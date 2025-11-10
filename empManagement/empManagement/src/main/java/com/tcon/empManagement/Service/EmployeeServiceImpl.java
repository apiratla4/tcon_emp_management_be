package com.tcon.empManagement.Service;

import com.tcon.empManagement.Dto.EmployeeCreateRequest;
import com.tcon.empManagement.Dto.EmployeeResponse;
import com.tcon.empManagement.Dto.EmployeeUpdateRequest;
import com.tcon.empManagement.Entity.Employee;
import com.tcon.empManagement.Repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeHistoryService employeeHistoryService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public EmployeeResponse createEmployee(EmployeeCreateRequest request, String createdBy) {
        String status = request.getStatus() != null ? request.getStatus() : "ACTIVE";

        Employee employee = Employee.builder()
                .title(request.getTitle())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .empRole(request.getEmpRole())
                .empId(request.getEmpId())
                .bloodGroup(request.getBloodGroup())
                .salary(request.getSalary())
                .status(status)
                .address(mapAddressFromCreateDto(request.getAddress()))
                .bankDetails(mapBankDetailsFromCreateDto(request.getBankDetails()))
                .emergencyContact(mapEmergencyContactFromCreateDto(request.getEmergencyContact()))
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        Employee saved = employeeRepository.save(employee);
        employeeHistoryService.recordCreate(saved, createdBy);

        return convertToResponse(saved);
    }

    @Override
    public EmployeeResponse updateEmployee(String id, EmployeeUpdateRequest request, String updatedBy) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));

        if ("INACTIVE".equals(employee.getStatus())) {
            throw new RuntimeException("Cannot update INACTIVE employee. Please activate the employee first.");
        }

        if (request.getTitle() != null) employee.setTitle(request.getTitle());
        if (request.getFirstName() != null) employee.setFirstName(request.getFirstName());
        if (request.getLastName() != null) employee.setLastName(request.getLastName());
        if (request.getEmail() != null) employee.setEmail(request.getEmail());
        if (request.getPassword() != null) employee.setPassword(passwordEncoder.encode(request.getPassword()));
        if (request.getPhoneNumber() != null) employee.setPhoneNumber(request.getPhoneNumber());
        if (request.getEmpRole() != null) employee.setEmpRole(request.getEmpRole());
        if (request.getBloodGroup() != null) employee.setBloodGroup(request.getBloodGroup());
        if (request.getSalary() != null) employee.setSalary(request.getSalary());
        if (request.getStatus() != null) employee.setStatus(request.getStatus());
        if (request.getAddress() != null) employee.setAddress(mapAddressFromUpdateDto(request.getAddress()));
        if (request.getBankDetails() != null) employee.setBankDetails(mapBankDetailsFromUpdateDto(request.getBankDetails()));
        if (request.getEmergencyContact() != null) employee.setEmergencyContact(mapEmergencyContactFromUpdateDto(request.getEmergencyContact()));

        employee.setUpdatedAt(Instant.now());
        Employee updated = employeeRepository.save(employee);
        employeeHistoryService.recordUpdate(updated, updatedBy);

        return convertToResponse(updated);
    }

    // Mapping from CreateRequest DTOs to Entity
    private Employee.Address mapAddressFromCreateDto(EmployeeCreateRequest.AddressDto dto) {
        if (dto == null) return null;
        return Employee.Address.builder()
                .address1(dto.getAddress1())
                .address2(dto.getAddress2())
                .country(dto.getCountry())
                .city(dto.getCity())
                .pincode(dto.getPincode())
                .build();
    }

    private Employee.BankDetails mapBankDetailsFromCreateDto(EmployeeCreateRequest.BankDetailsDto dto) {
        if (dto == null) return null;
        return Employee.BankDetails.builder()
                .bankAccount(dto.getBankAccount())
                .ifscCode(dto.getIfscCode())
                .bankName(dto.getBankName())
                .branchName(dto.getBranchName())
                .build();
    }

    private Employee.EmergencyContact mapEmergencyContactFromCreateDto(EmployeeCreateRequest.EmergencyContactDto dto) {
        if (dto == null) return null;
        return Employee.EmergencyContact.builder()
                .name(dto.getName())
                .contactNumber(dto.getContactNumber())
                .relation(dto.getRelation())
                .build();
    }

    // Mapping from UpdateRequest DTOs to Entity
    private Employee.Address mapAddressFromUpdateDto(EmployeeUpdateRequest.AddressDto dto) {
        if (dto == null) return null;
        return Employee.Address.builder()
                .address1(dto.getAddress1())
                .address2(dto.getAddress2())
                .country(dto.getCountry())
                .city(dto.getCity())
                .pincode(dto.getPincode())
                .build();
    }

    private Employee.BankDetails mapBankDetailsFromUpdateDto(EmployeeUpdateRequest.BankDetailsDto dto) {
        if (dto == null) return null;
        return Employee.BankDetails.builder()
                .bankAccount(dto.getBankAccount())
                .ifscCode(dto.getIfscCode())
                .bankName(dto.getBankName())
                .branchName(dto.getBranchName())
                .build();
    }

    private Employee.EmergencyContact mapEmergencyContactFromUpdateDto(EmployeeUpdateRequest.EmergencyContactDto dto) {
        if (dto == null) return null;
        return Employee.EmergencyContact.builder()
                .name(dto.getName())
                .contactNumber(dto.getContactNumber())
                .relation(dto.getRelation())
                .build();
    }

    // Mapping from Entity to Response DTO
    private EmployeeResponse convertToResponse(Employee employee) {
        return EmployeeResponse.builder()
                .id(employee.getId())
                .title(employee.getTitle())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .phoneNumber(employee.getPhoneNumber())
                .empRole(employee.getEmpRole())
                .empId(employee.getEmpId())
                .bloodGroup(employee.getBloodGroup())
                .salary(employee.getSalary())
                .status(employee.getStatus())
                .address(mapAddressToResponseDto(employee.getAddress()))
                .bankDetails(mapBankDetailsToResponseDto(employee.getBankDetails()))
                .emergencyContact(mapEmergencyContactToResponseDto(employee.getEmergencyContact()))
                .createdAt(employee.getCreatedAt())
                .updatedAt(employee.getUpdatedAt())
                .build();
    }

    private EmployeeResponse.AddressDto mapAddressToResponseDto(Employee.Address address) {
        if (address == null) return null;
        return EmployeeResponse.AddressDto.builder()
                .address1(address.getAddress1())
                .address2(address.getAddress2())
                .country(address.getCountry())
                .city(address.getCity())
                .pincode(address.getPincode())
                .build();
    }

    private EmployeeResponse.BankDetailsDto mapBankDetailsToResponseDto(Employee.BankDetails bankDetails) {
        if (bankDetails == null) return null;
        return EmployeeResponse.BankDetailsDto.builder()
                .bankAccount(bankDetails.getBankAccount())
                .ifscCode(bankDetails.getIfscCode())
                .bankName(bankDetails.getBankName())
                .branchName(bankDetails.getBranchName())
                .build();
    }

    private EmployeeResponse.EmergencyContactDto mapEmergencyContactToResponseDto(Employee.EmergencyContact emergencyContact) {
        if (emergencyContact == null) return null;
        return EmployeeResponse.EmergencyContactDto.builder()
                .name(emergencyContact.getName())
                .contactNumber(emergencyContact.getContactNumber())
                .relation(emergencyContact.getRelation())
                .build();
    }

    // Implement remaining methods from interface...
    @Override
    public void deleteEmployee(String id, String deletedBy) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
        employeeHistoryService.recordDelete(employee, deletedBy);
        employeeRepository.deleteById(id);
    }

    @Override
    public EmployeeResponse getEmployeeById(String id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
        return convertToResponse(employee);
    }

    @Override
    public EmployeeResponse getEmployeeByEmpId(String empId) {
        Employee employee = employeeRepository.findByEmpId(empId)
                .orElseThrow(() -> new RuntimeException("Employee not found with empId: " + empId));
        return convertToResponse(employee);
    }

    @Override
    public EmployeeResponse getEmployeeByEmail(String email) {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Employee not found with email: " + email));
        return convertToResponse(employee);
    }

    @Override
    public List<EmployeeResponse> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeResponse> getActiveEmployees() {
        return employeeRepository.findByStatus("ACTIVE").stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeResponse> getInactiveEmployees() {
        return employeeRepository.findByStatus("INACTIVE").stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeResponse> getEmployeesByRole(String empRole) {
        return employeeRepository.findByEmpRole(empRole).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeResponse activateEmployee(String id, String activatedBy) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
        employee.setStatus("ACTIVE");
        employee.setUpdatedAt(Instant.now());
        Employee updated = employeeRepository.save(employee);
        employeeHistoryService.recordUpdate(updated, activatedBy);
        return convertToResponse(updated);
    }

    @Override
    public EmployeeResponse deactivateEmployee(String id, String deactivatedBy) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
        employee.setStatus("INACTIVE");
        employee.setUpdatedAt(Instant.now());
        Employee updated = employeeRepository.save(employee);
        employeeHistoryService.recordUpdate(updated, deactivatedBy);
        return convertToResponse(updated);
    }
}
