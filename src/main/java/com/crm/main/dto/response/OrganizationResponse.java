package com.crm.main.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrganizationResponse {

    private Long id;

    private String name;

    private String address;

    private String city;

    private String country;

    private String email;

    private String description;

    private Instant createdAt;

    private Instant updatedAt;

}
