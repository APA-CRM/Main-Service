package com.crm.main.dto.request;

import com.crm.sharedlib.core.dto.request.UserFilterRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAndRolesFilterRequest extends UserFilterRequest {

    private List<Long> rolesId;

}
