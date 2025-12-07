package com.crm.main.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum OrganizationMessage {

    USER_HAS_BEEN_ADDED_TO_THE_ORGANIZATION(
            "User has been added to the organization",
            "User '%s' has been added to the organization with role '%s'"
    );

    private final String title;
    private final String messageCode = name();
    private final String message;

}
