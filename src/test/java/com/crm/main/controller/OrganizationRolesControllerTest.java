package com.crm.main.controller;

import com.crm.main.feign.AuthClient;
import com.crm.sharedlib.dto.request.ResourceWithActionsRequest;
import com.crm.sharedlib.dto.request.RoleFilterRequest;
import com.crm.sharedlib.dto.request.RoleRequest;
import com.crm.sharedlib.dto.response.RestResponsePage;
import com.crm.sharedlib.dto.response.RoleResponse;
import com.crm.sharedlib.enums.Action;
import com.crm.sharedlib.enums.Resource;
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
import java.util.List;

import static com.crm.sharedlib.consts.CrmConstants.ORGANIZATION_ID_HEADER_NAME;
import static com.crm.sharedlib.consts.CrmConstants.USER_ID_HEADER_NAME;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Sql(scripts = "classpath:sql/insertTestOrganizations.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:sql/deleteTestOrganization.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class OrganizationRolesControllerTest extends BaseIntegrationTest {

    private final static String BASE_URI = "/api/organizations";

    @MockitoBean
    private AuthClient authClient;

    @MockitoBean
    private RabbitTemplate rabbitTemplate;

    @Test
    @DisplayName("Get roles of organization when user not in organization expected forbidden response")
    public void getRolesOfOrganizationWhenUserNotInOrganizationExpectedForbidden() {
        final long organizationId = 100;

        given()
                .contentType(ContentType.JSON)
                .header(USER_ID_HEADER_NAME, 100)
                .header(ORGANIZATION_ID_HEADER_NAME, 100)
                .when()
                .get(BASE_URI + "/{organizationId}/roles", organizationId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .body("message", is("User is not in the organization"));

    }

    @Test
    @DisplayName("Get roles of organization expected success response")
    public void getRolesOfOrganizationExpectedSuccess() throws JsonProcessingException {
        final long organizationId = 100;

        final String responseString = """
                {
                        "id": 100,
                        "name": "Admin",
                        "accessControls": [
                            {
                                "resource": "All",
                                "actions": [
                                    "All"
                                ]
                            }
                        ],
                        "createdAt": null,
                        "updateAt": null
                    }
                """;

        RoleResponse response = objectMapper.readValue(responseString, RoleResponse.class);

        Mockito.when(authClient.getRole(Mockito.any()))
                .thenReturn(Collections.singletonList(response));

        given()
                .contentType(ContentType.JSON)
                .header(USER_ID_HEADER_NAME, 1)
                .header(ORGANIZATION_ID_HEADER_NAME, 100)
                .when()
                .get(BASE_URI + "/{organizationId}/roles", organizationId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("[0].id", is(response.getId().intValue()))
                .body("[0].name", is(response.getName()))
                .body("[0].accessControls[0].resource", is(response.getAccessControls().getFirst().getResource().getName()));

    }

    @Test
    @DisplayName("Create role for organization expected success response")
    public void createRoleExpectedSuccess() throws JsonProcessingException {

        final long organizationId = 100;

        ResourceWithActionsRequest resource1 = new ResourceWithActionsRequest();
        resource1.setResource(Resource.USERS);
        resource1.setActions(List.of(Action.CREATE, Action.DELETE, Action.UPDATE, Action.READ));

        ResourceWithActionsRequest resource2 = new ResourceWithActionsRequest();

        resource2.setResource(Resource.ORGANIZATIONS);
        resource2.setActions(Collections.singletonList(Action.ALL));

        RoleRequest request = new RoleRequest();

        request.setName("Admin");
        request.setResources(List.of(resource1, resource2));

        final String responseString = """
                {
                    "id": 1,
                    "name": "Admin",
                    "accessControls": [
                        {
                            "resource": "Users",
                            "actions": [
                                "All"
                            ]
                        },
                        {
                            "resource": "Organizations",
                            "actions": [
                                "All"
                            ]
                        }
                    ],
                    "createdAt": "2025-05-24T10:33:45.753224247Z",
                    "updateAt": "2025-05-24T10:33:45.753224247Z"
                }
                """;

        RoleResponse roleResponse = objectMapper.readValue(responseString, RoleResponse.class);

        Mockito.when(authClient.createRole(Mockito.any()))
                .thenReturn(roleResponse);

        given()
                .contentType(ContentType.JSON)
                .header(USER_ID_HEADER_NAME, 1)
                .header(ORGANIZATION_ID_HEADER_NAME, 100)
                .when()
                .body(request)
                .post(BASE_URI + "/{organizationId}/roles", organizationId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("id", notNullValue())
                .body("name", is("Admin"))
                .body("accessControls", hasItems(
                        hasEntry("resource", Resource.USERS.getName()),
                        hasEntry("resource", Resource.ORGANIZATIONS.getName())
                ))
                .body("createdAt", notNullValue())
                .body("updateAt", notNullValue());

    }

    @Test
    @DisplayName("Filter organization roles expected success response")
    public void filterOrganizationRoleExpectedSuccess() throws JsonProcessingException {
        final Long organizationId = 100L;

        final String responseString = """
                {
                    "content": [
                        {
                            "id": 200,
                            "name": "Manager",
                            "accessControls": [
                                {
                                    "resource": "USERS",
                                    "actions": [
                                        "READ",
                                        "DELETE",
                                        "CREATE",
                                        "UPDATE"
                                    ]
                                }
                            ],
                            "createdAt": null,
                            "updateAt": null
                        }
                    ],
                    "page": {
                        "size": 5,
                        "number": 0,
                        "totalElements": 1,
                        "totalPages": 1
                    }
                }
                """;

        RestResponsePage response = objectMapper.readValue(responseString, RestResponsePage.class);

        Mockito.when(authClient.filterRoles(Mockito.any()))
                .thenReturn(response);

        given()
                .contentType(ContentType.JSON)
                .header(USER_ID_HEADER_NAME, 1)
                .header(ORGANIZATION_ID_HEADER_NAME, 100)
                .when()
                .queryParam("page", 0)
                .queryParam("size", 5)
                .get(BASE_URI + "/{organizationId}/roles/filter", organizationId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("content", notNullValue())
                .body("page", notNullValue());

    }

    @Test
    @DisplayName("Delete organization role expected success response")
    public void deleteOrganizationRoleExpectedSuccess() {

        final long organizationId = 100;

        final long roleId = 4L;

        given()
                .contentType(ContentType.JSON)
                .header(USER_ID_HEADER_NAME, 1)
                .header(ORGANIZATION_ID_HEADER_NAME, 100)
                .when()
                .delete(BASE_URI + "/{organizationId}/roles/{roleId}", organizationId, roleId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("Delete organization when 'Admin' role expected forbidden response")
    public void deleteOrganizationRoleWhenAdminRoleExpectedForbidden() {

        final long organizationId = 100;

        final long roleId = 1L;

        given()
                .contentType(ContentType.JSON)
                .header(USER_ID_HEADER_NAME, 1)
                .header(ORGANIZATION_ID_HEADER_NAME, 100)
                .when()
                .delete(BASE_URI + "/{organizationId}/roles/{roleId}", organizationId, roleId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .body("message", is("You can't delete this role"));
    }

}