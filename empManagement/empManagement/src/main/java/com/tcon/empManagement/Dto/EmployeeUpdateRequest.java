package com.tcon.empManagement.Dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeUpdateRequest {

    private String firstName;

    private String lastName;
    private String title;
    @Email @Size(max = 120)
    private String email;

    @Min(6000000000L) @Max(9999999999L)
    private Long phoneNumber;

    @Size(max = 40)
    private String empRole;

    @Size(max = 10)
    @Pattern(regexp = "^(A|B|AB|O)[+-]?$")
    private String bloodGroup;
    private String password;
    @DecimalMin(value = "0.0", inclusive = true)
    private Double salary;

    private EmployeeCreateRequest.AddressDto address;

    private EmployeeCreateRequest.BankDetailsDto bankDetails;

    private EmployeeCreateRequest.EmergencyContactDto emergencyContact;

}
