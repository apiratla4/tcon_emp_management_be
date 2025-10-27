package com.tcon.empManagement.Service;
import com.tcon.empManagement.Entity.Employee;
import com.tcon.empManagement.Repository.EmployeeRepository;
import com.tcon.empManagement.Dto.EmployeeCreateRequest;
import com.tcon.empManagement.Dto.EmployeeResponse;
import com.tcon.empManagement.Dto.EmployeeUpdateRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository repo;

    @Override
    public EmployeeResponse create(EmployeeCreateRequest request) {
        log.info("Creating employee empId={}, email={}", request.getEmpId(), request.getEmail());
        // try/catch only for predictable persistence errors; let ControllerAdvice log stack trace
        if (repo.existsByEmail(request.getEmail())) {
            log.warn("Create failed: duplicate email={}", request.getEmail());
            throw new DuplicateKeyException("Email already exists: " + request.getEmail());
        }
        if (repo.existsByEmpId(request.getEmpId())) {
            log.warn("Create failed: duplicate empId={}", request.getEmpId());
            throw new DuplicateKeyException("empId already exists: " + request.getEmpId());
        }

        Employee saved = repo.save(mapToEntity(request));
        log.info("Employee created id={}", saved.getId());
        return mapToResponse(saved);
    }

    @Override
    public EmployeeResponse updateById(String id, EmployeeUpdateRequest request) {
        log.info("Updating employee id={}", id);
        Employee existing = repo.findById(id)
                .orElseThrow(() -> {
                    log.warn("Update failed: not found id={}", id);
                    return new NoSuchElementException("Employee not found: " + id);
                });

        if (request.getEmail() != null && !request.getEmail().equalsIgnoreCase(existing.getEmail())) {
            if (repo.existsByEmail(request.getEmail())) {
                log.warn("Update failed: duplicate email={} for id={}", request.getEmail(), id);
                throw new DuplicateKeyException("Email already exists: " + request.getEmail());
            }
            existing.setEmail(request.getEmail());
        }
        if (request.getFirstName() != null) existing.setFirstName(request.getFirstName());
        if (request.getLastName() != null) existing.setLastName(request.getLastName());
        if (request.getPhoneNumber() != null) existing.setPhoneNumber(request.getPhoneNumber());
        if (request.getEmpRole() != null) existing.setEmpRole(request.getEmpRole());
        if (request.getBloodGroup() != null) existing.setBloodGroup(request.getBloodGroup());
        if (request.getSalary() != null) existing.setSalary(request.getSalary());

        if (request.getAddress() != null) {
            Employee.Address a = Employee.Address.builder()
                    .address1(request.getAddress().getAddress1())
                    .address2(request.getAddress().getAddress2())
                    .country(request.getAddress().getCountry())
                    .city(request.getAddress().getCity())
                    .pincode(request.getAddress().getPincode())
                    .build();
            existing.setAddress(a);
        }
        if (request.getBankDetails() != null) {
            Employee.BankDetails b = Employee.BankDetails.builder()
                    .bankAccount(request.getBankDetails().getBankAccount())
                    .ifscCode(request.getBankDetails().getIfscCode())
                    .bankName(request.getBankDetails().getBankName())
                    .branchName(request.getBankDetails().getBranchName())
                    .build();
            existing.setBankDetails(b);
        }
        if (request.getEmergencyContact() != null) {
            Employee.EmergencyContact ec = Employee.EmergencyContact.builder()
                    .name(request.getEmergencyContact().getName())
                    .contactNumber(request.getEmergencyContact().getContactNumber())
                    .relation(request.getEmergencyContact().getRelation())
                    .build();
            existing.setEmergencyContact(ec);
        }

        Employee saved = repo.save(existing);
        log.info("Employee updated id={}", saved.getId());
        return mapToResponse(saved);
    }

    @Override
    public EmployeeResponse getById(String id) {
        log.info("Fetching employee by id={}", id);
        return repo.findById(id)
                .map(emp -> {
                    log.info("Fetched employee id={}", id);
                    return mapToResponse(emp);
                })
                .orElseThrow(() -> {
                    log.warn("Get failed: not found id={}", id);
                    return new NoSuchElementException("Employee not found: " + id);
                });
    }

    @Override
    public EmployeeResponse getByEmpId(String empId) {
        log.info("Fetching employee by empId={}", empId);
        return repo.findByEmpId(empId)
                .map(emp -> {
                    log.info("Fetched employee empId={}", empId);
                    return mapToResponse(emp);
                })
                .orElseThrow(() -> {
                    log.warn("Get failed: not found empId={}", empId);
                    return new NoSuchElementException("Employee not found: " + empId);
                });
    }

    @Override
    public Page<EmployeeResponse> list(Pageable pageable) {
        log.info("Listing employees page={} size={}", pageable.getPageNumber(), pageable.getPageSize());
        return repo.findAll(pageable).map(this::mapToResponse);
    }

    @Override
    public void deleteById(String id) {
        log.info("Deleting employee id={}", id);
        if (!repo.existsById(id)) {
            log.warn("Delete failed: not found id={}", id);
            throw new NoSuchElementException("Employee not found: " + id);
        }
        repo.deleteById(id);
        log.info("Deleted employee id={}", id);
    }

    private Employee mapToEntity(EmployeeCreateRequest r) {
        return Employee.builder()
                .firstName(r.getFirstName())
                .lastName(r.getLastName())
                .email(r.getEmail())
                .phoneNumber(r.getPhoneNumber())
                .empRole(r.getEmpRole())
                .empId(r.getEmpId())
                .bloodGroup(r.getBloodGroup())
                .salary(r.getSalary())
                .address(Employee.Address.builder()
                        .address1(r.getAddress().getAddress1())
                        .address2(r.getAddress().getAddress2())
                        .country(r.getAddress().getCountry())
                        .city(r.getAddress().getCity())
                        .pincode(r.getAddress().getPincode())
                        .build())
                .bankDetails(Employee.BankDetails.builder()
                        .bankAccount(r.getBankDetails().getBankAccount())
                        .ifscCode(r.getBankDetails().getIfscCode())
                        .bankName(r.getBankDetails().getBankName())
                        .branchName(r.getBankDetails().getBranchName())
                        .build())
                .emergencyContact(Employee.EmergencyContact.builder()
                        .name(r.getEmergencyContact().getName())
                        .contactNumber(r.getEmergencyContact().getContactNumber())
                        .relation(r.getEmergencyContact().getRelation())
                        .build())
                .build();
    }

    private EmployeeResponse mapToResponse(Employee e) {
        return EmployeeResponse.builder()
                .id(e.getId())
                .firstName(e.getFirstName())
                .lastName(e.getLastName())
                .email(e.getEmail())
                .phoneNumber(e.getPhoneNumber())
                .empRole(e.getEmpRole())
                .empId(e.getEmpId())
                .bloodGroup(e.getBloodGroup())
                .salary(e.getSalary())
                .address(EmployeeResponse.Address.builder()
                        .address1(e.getAddress().getAddress1())
                        .address2(e.getAddress().getAddress2())
                        .country(e.getAddress().getCountry())
                        .city(e.getAddress().getCity())
                        .pincode(e.getAddress().getPincode())
                        .build())
                .bankDetails(EmployeeResponse.BankDetails.builder()
                        .bankAccount(e.getBankDetails().getBankAccount())
                        .ifscCode(e.getBankDetails().getIfscCode())
                        .bankName(e.getBankDetails().getBankName())
                        .branchName(e.getBankDetails().getBranchName())
                        .build())
                .emergencyContact(EmployeeResponse.EmergencyContact.builder()
                        .name(e.getEmergencyContact().getName())
                        .contactNumber(e.getEmergencyContact().getContactNumber())
                        .relation(e.getEmergencyContact().getRelation())
                        .build())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .build();
    }
}
