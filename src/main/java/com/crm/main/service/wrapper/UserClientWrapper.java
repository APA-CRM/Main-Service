package com.crm.main.service.wrapper;

import com.crm.main.feign.AuthClient;
import com.crm.sharedlib.core.dto.request.UserFilterRequest;
import com.crm.sharedlib.core.dto.response.UserResponse;
import com.crm.sharedlib.core.dto.response.UserWithRoleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserClientWrapper {

    private final AuthClient authClient;

    public PagedModel<UserWithRoleResponse> filterUsers(UserFilterRequest request) {
        return authClient.filterUsers(request);
    }

    public UserResponse getUserById(Long userId) {
        return authClient.getUserById(userId);
    }

}
