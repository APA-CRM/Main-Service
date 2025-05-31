package com.crm.main.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateOrganizationRequest {

    @NotBlank(message = "Name of organization can't be blank")
    private String name;

    private String address;

    private String city;

    private String country;

    @Email(message = "Email must follow email pattern")
    private String email;

}
