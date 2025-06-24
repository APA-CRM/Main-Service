package com.crm.main.dto.response;

import com.crm.main.enums.InvitationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationInvitationResponse {

    private UUID id;

    private OrganizationResponse organization;

    private Long userId;

    private InvitationStatus status;

    private Long invitorId;

    private Instant expiredAt;

    private Instant createdAt;

    private Instant updatedAt;

}
