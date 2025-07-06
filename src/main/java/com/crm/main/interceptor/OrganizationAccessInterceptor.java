package com.crm.main.interceptor;

import com.crm.main.persistance.entity.OrganizationUser;
import com.crm.main.service.OrganizationUserService;
import com.crm.sharedlib.exception.response.CrmErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Optional;

import static com.crm.sharedlib.consts.CrmConstants.ORGANIZATION_ID_HEADER_NAME;
import static com.crm.sharedlib.consts.CrmConstants.USER_ID_HEADER_NAME;
import static java.util.Objects.isNull;

@RequiredArgsConstructor
public class OrganizationAccessInterceptor implements PublicEndpointInterceptor {

    private final OrganizationUserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final List<Endpoint> PUBLIC_ENDPOINTS = List.of(
            new Endpoint(HttpMethod.GET, "/api/organizations/*/preview"),
            new Endpoint(HttpMethod.PATCH, "/api/organizations/invitations/**"),
            new Endpoint(HttpMethod.GET, "/api/organizations/invitations/**"),
            new Endpoint(HttpMethod.GET, "/api/organizations"),
            new Endpoint(HttpMethod.GET, "/api/organizations/*"),
            new Endpoint(HttpMethod.POST, "/api/organizations"),
            new Endpoint(HttpMethod.GET, "/api/internal/**")
    );

    @Override
    public List<Endpoint> getPublicEndpoints() {
        return PUBLIC_ENDPOINTS;
    }

    @Override
    public boolean intercept(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {

        String organizationId = request.getHeader(ORGANIZATION_ID_HEADER_NAME);

        String userId = request.getHeader(USER_ID_HEADER_NAME);

        if (isNull(userId) || isNull(organizationId)) {
            respondWithError(HttpStatus.FORBIDDEN.value(), "Organization ID or user ID is not specified", response);
            return false;
        }

        Optional<OrganizationUser> organizationUser =
                userService.getByOrganizationAndUserId(
                        Long.valueOf(organizationId), Long.valueOf(userId)
                );

        if (organizationUser.isEmpty()) {
            respondWithError(HttpStatus.FORBIDDEN.value(), "User is not in the organization", response);
            return false;
        }

        return true;
    }

    @SneakyThrows
    private void respondWithError(int httpStatus, String message, HttpServletResponse response) {

        CrmErrorResponse errorResponse = new CrmErrorResponse(message);

        String value = objectMapper.writeValueAsString(errorResponse);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(value);
        response.setStatus(httpStatus);
    }

}
