package com.crm.main.interceptor;

import com.crm.main.persistance.entity.OrganizationUser;
import com.crm.main.service.OrganizationUserService;
import com.crm.sharedlib.interceptor.Endpoint;
import com.crm.sharedlib.interceptor.PublicEndpointInterceptor;
import com.crm.sharedlib.utils.OrganizationIdExtractor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static com.crm.sharedlib.consts.CrmConstants.USER_ID_HEADER_NAME;
import static java.util.Collections.singletonList;
import static java.util.Objects.isNull;

@RequiredArgsConstructor
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
    ) throws Exception {

        Long organizationId = OrganizationIdExtractor.extractOrganizationIdFromRequest(request);

        String userId = request.getHeader(USER_ID_HEADER_NAME);

        if (isNull(userId) || isNull(organizationId)) {
            super.respondWithError(HttpStatus.FORBIDDEN.value(), "Organization ID or user ID is not specified", response);
            return false;
        }

        Optional<OrganizationUser> organizationUser =
                userService.getByOrganizationAndUserId(
                        organizationId, Long.valueOf(userId)
                );

        if (organizationUser.isEmpty()) {
            super.respondWithError(HttpStatus.FORBIDDEN.value(), "User is not in the organization", response);
            return false;
        }

        return true;
    }

}
