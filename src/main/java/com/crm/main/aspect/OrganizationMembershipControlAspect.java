package com.crm.main.aspect;

import com.crm.main.annotations.OrganizationId;
import com.crm.main.annotations.UserId;
import com.crm.main.service.OrganizationUserService;
import com.crm.sharedlib.core.exception.ForbiddenException;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

import static java.util.Objects.isNull;

@Aspect
@Component
@RequiredArgsConstructor
public class OrganizationMembershipControlAspect {

    private final OrganizationUserService userService;

    @Before("@annotation(com.crm.main.annotations.RequiresOrganizationMembership)")
    public void checkOrganizationMembership(JoinPoint joinPoint) {
        Long userId = getUserIdOrThrowException(joinPoint);
        Long organizationId = getOrganizationIdOrThrowException(joinPoint);

        // TODO: Think about a caching
        boolean userExistsInOrganization = userService.isUserExistsInOrganization(organizationId, userId);

        if (!userExistsInOrganization) {
            throw new ForbiddenException("User is not in the organization");
        }
    }

    private Long getUserIdOrThrowException(JoinPoint joinPoint) {
        Object userId = getAnnotatedParameterValue(joinPoint, UserId.class);

        if (isNull(userId)) {
            throw new IllegalStateException("No parameter annotated with @UserId");
        }

        return (Long) userId;
    }

    private Long getOrganizationIdOrThrowException(JoinPoint joinPoint) {
        Object organizationId = getAnnotatedParameterValue(joinPoint, OrganizationId.class);

        if (isNull(organizationId)) {
            throw new IllegalStateException("No parameter annotated with @OrganizationId");
        }

        return (Long) organizationId;
    }

    private @Nullable Object getAnnotatedParameterValue(
            JoinPoint joinPoint,
            Class<? extends Annotation> annotationClass
    ) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Annotation[][] parameterAnnotations = methodSignature.getMethod().getParameterAnnotations();
        Object[] args = joinPoint.getArgs();

        for (int i = 0; i < args.length; i++) {
            for (Annotation annotation : parameterAnnotations[i]) {
                if (annotationClass.isInstance(annotation)) {
                    return args[i];
                }
            }
        }

        return null;
    }
}
