package com.crm.main.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

public interface PublicEndpointInterceptor extends HandlerInterceptor {

    AntPathMatcher pathMatcher = new AntPathMatcher();

    boolean intercept(HttpServletRequest request, HttpServletResponse response, Object handler);

    List<Endpoint> getPublicEndpoints();

    default boolean isPublicEndpoint(HttpServletRequest request) {
        HttpMethod httpMethod = HttpMethod.valueOf(request.getMethod());
        String uri = request.getRequestURI();

        return getPublicEndpoints().stream().anyMatch(
                endpoint -> endpoint.getHttpMethod().equals(httpMethod) &&
                        pathMatcher.match(endpoint.getUri(), uri)
        );
    }

    @Override
    default boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (isPublicEndpoint(request)) {
            return true;
        }

        return intercept(request, response, handler);
    }
}
