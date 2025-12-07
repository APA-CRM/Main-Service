package com.crm.main.controller;

import com.crm.main.BaseIntegrationTest;
import com.crm.main.feign.AuthClient;
import com.crm.sharedlib.core.dto.response.RoleResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;

import java.util.Collections;

import static com.crm.sharedlib.core.consts.CrmHeaders.ORGANIZATION_ID_HEADER_NAME;
import static com.crm.sharedlib.core.consts.CrmHeaders.USER_ID_HEADER_NAME;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@Sql(scripts = "classpath:sql/insertTestOrganizations.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:sql/deleteTestOrganization.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class OrganizationUserRoleControllerTest extends BaseIntegrationTest {

    private final static String BASE_URI = "/api/organizations";

    @MockitoBean
    private AuthClient authClient;

    @MockitoBean
    private RabbitTemplate rabbitTemplate;

    @Test
    @DisplayName("Add role for user in organization expected success response")
    public void addRoleForUserInOrganizationExpectedSuccess() throws JsonProcessingException {
        final Long userId = 1L;
        final Long organizationId = 100L;
        final Long roleId = 4L;

        final String responseString = """
                {
                    "id": 1,
                    "name": "Admin",
                    "accessControls": [
                        {
                            "resource": "Organizations",
                            "actions": [
                                "All"
                            ]
                        },
                        {
                            "resource": "Users",
                            "actions": [
                                "All"
                            ]
                        }
                    ],
                    "createdAt": "2025-05-25T10:40:26.011183972Z",
                    "updateAt": "2025-05-25T10:40:26.011183972Z"
                }
                """;

        RoleResponse roleResponse = objectMapper.readValue(responseString, RoleResponse.class);

        Mockito.when(authClient.getRole(Mockito.anyList()))
                .thenReturn(Collections.singletonList(roleResponse));

        given()
                .contentType(ContentType.JSON)
                .header(USER_ID_HEADER_NAME, 1)
                .header(ORGANIZATION_ID_HEADER_NAME, 100)
                .when()
                .put(BASE_URI + "/{organizationId}/users/{userId}/roles/{roleId}",
                        organizationId, userId, roleId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("", notNullValue());
    }

    @Test
    @DisplayName("Add role for user in organization when user already has the same role expected conflict response")
    public void addRoleForUserInOrganizationWhenUserAlreadyHasTheSameRoleExpectedConflict() {
        final Long userId = 1L;
        final Long organizationId = 100L;
        final Long roleId = 1L;

        given()
                .contentType(ContentType.JSON)
                .header(USER_ID_HEADER_NAME, 1)
                .header(ORGANIZATION_ID_HEADER_NAME, 100)
                .when()
                .put(BASE_URI + "/{organizationId}/users/{userId}/roles/{roleId}",
                        organizationId, userId, roleId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.CONFLICT.value())
                .body("message", is("User already has this role"));

    }

    @Test
    @DisplayName("Add role for user in organization when role not found expected not found response")
    public void addRoleForUserInOrganizationWhenRoleNotFoundExpectedNotFound() {
        final Long userId = 1L;
        final Long organizationId = 100L;
        final Long roleId = 100L;

        given()
                .contentType(ContentType.JSON)
                .header(USER_ID_HEADER_NAME, 1)
                .header(ORGANIZATION_ID_HEADER_NAME, 100)
                .when()
                .put(BASE_URI + "/{organizationId}/users/{userId}/roles/{roleId}",
                        organizationId, userId, roleId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("message", is("Role is not found"));

    }

    @Test
    @DisplayName("Add role for user in organization when user not found expected not found response")
    public void addRoleForUserInOrganizationWhenUserNotFoundExpectedNotFound() {
        final Long userId = 100L;
        final Long organizationId = 100L;
        final Long roleId = 1L;

        given()
                .contentType(ContentType.JSON)
                .header(USER_ID_HEADER_NAME, 1)
                .header(ORGANIZATION_ID_HEADER_NAME, 100)
                .when()
                .put(BASE_URI + "/{organizationId}/users/{userId}/roles/{roleId}",
                        organizationId, userId, roleId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("message", is("User is not found"));

    }

    @Test
    @DisplayName("Remove role for user in organization expected success response")
    public void removeRoleForUserOrganizationExpectedSuccess() {
        final Long userId = 3L;
        final Long organizationId = 100L;
        final Long roleId = 4L;

        given()
                .contentType(ContentType.JSON)
                .header(USER_ID_HEADER_NAME, 1)
                .header(ORGANIZATION_ID_HEADER_NAME, 100)
                .when()
                .delete(BASE_URI + "/{organizationId}/users/{userId}/roles/{roleId}",
                        organizationId, userId, roleId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value());

    }

    @Test
    @DisplayName("Remove role for user in organization when user has only one role expected success response")
    public void removeRoleForUserOrganizationWhenUserHasOnlyOneRoleExpectedSuccess() {
        final Long userId = 1L;
        final Long organizationId = 100L;
        final Long roleId = 1L;

        given()
                .contentType(ContentType.JSON)
                .header(USER_ID_HEADER_NAME, 1)
                .header(ORGANIZATION_ID_HEADER_NAME, 100)
                .when()
                .delete(BASE_URI + "/{organizationId}/users/{userId}/roles/{roleId}",
                        organizationId, userId, roleId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .body("message", is("Role can't be unassigned — user has no other roles"));

    }

}