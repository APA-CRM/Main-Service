package com.crm.main.interceptor;

import com.crm.main.service.OrganizationUserService;
import com.crm.sharedlib.core.interceptor.Endpoint;
import com.crm.sharedlib.core.interceptor.PublicEndpointInterceptor;
import com.crm.sharedlib.core.utils.OrganizationIdExtractor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.util.List;

import static com.crm.sharedlib.core.consts.CrmHeaders.USER_ID_HEADER_NAME;
import static java.util.Collections.singletonList;
import static java.util.Objects.isNull;

@RequiredArgsConstructor
// TODO: Instead of Interceptor use Spring AoP mechanism
public class OrganizationAccessInterceptor extends PublicEndpointInterceptor {

    private final OrganizationUserService userService;

    private final List<Endpoint> PUBLIC_ENDPOINTS = List.of(
            new Endpoint(singletonList(HttpMethod.GET), "/api/organizations/*/preview"),
            new Endpoint(List.of(HttpMethod.PATCH, HttpMethod.GET), "/api/organizations/invitations/**"),
            new Endpoint(List.of(HttpMethod.GET, HttpMethod.POST), "/api/organizations"),
            new Endpoint(singletonList(HttpMethod.GET), "/api/organizations/*"),
            new Endpoint(singletonList(HttpMethod.GET), "/api/internal/organizations/*/users/**")
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

        Long organizationId = OrganizationIdExtractor.extractOrganizationIdFromRequest(request);

        String userId = request.getHeader(USER_ID_HEADER_NAME);

        if (isNull(userId) || isNull(organizationId)) {
            super.respondWithError(HttpStatus.FORBIDDEN.value(), "Organization ID or user ID is not specified", response);
            return false;
        }

        // TODO: Think about a caching
        boolean userExistsInOrganization = userService.isUserExistsInOrganization(organizationId, Long.valueOf(userId));

        if (!userExistsInOrganization) {
            super.respondWithError(HttpStatus.FORBIDDEN.value(), "User is not in the organization", response);
            return false;
        }

        return true;
    }

}
