package com.crm.main.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrganizationFileResponse {

    private UUID id;

    private UUID fileId;

    private OrganizationResponse organization;

    private Instant createdAt;

    private Instant updatedAt;

}
