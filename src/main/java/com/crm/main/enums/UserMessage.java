package com.crm.main.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UserMessage {

    ROLE_HAS_BEEN_ASSIGNED(
            "Role has been assigned",
            "Role '%s' has been assigned to you"
    ),
    ROLE_HAS_BEEN_UNASSIGNED(
            "Role has been unassigned",
            "Role '%s' has been unassigned from you"
    ),
    USER_HAS_DECLINED_AN_INVITATION(
            "User has declined an invitation",
            "User '%s' has declined an invitation to an organization '%s'"
    ),
    USER_HAS_ACCEPTED_AN_INVITATION(
            "User has accepted an invitation",
            "User '%s' has accepted an invitation to an organization '%s'"
    ),
    USER_HAS_BEEN_INVITED_TO_ORGANIZATION(
            "You have been invited to an organization",
            "You have been invited to organization '%s'"
    );

    private final String title;
    private final String messageCode = name();
    private final String message;

}
