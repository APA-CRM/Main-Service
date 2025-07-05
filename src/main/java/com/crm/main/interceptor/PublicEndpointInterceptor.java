package com.crm.main.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

public interface PublicEndpointInterceptor extends HandlerInterceptor {

    boolean intercept(HttpServletRequest request, HttpServletResponse response, Object handler);

    List<Endpoint> getPublicEndpoints();

    boolean isPublicEndpoint(HttpServletRequest request);

    @Override
    default boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (isPublicEndpoint(request)) {
            return true;
        }

        return intercept(request, response, handler);
    }
}
