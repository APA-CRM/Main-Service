package com.crm.main.controller;

import com.crm.main.BaseIntegrationTest;
import com.crm.main.feign.AuthClient;
import com.crm.sharedlib.dto.response.RestResponsePage;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static com.crm.sharedlib.consts.CrmConstants.ORGANIZATION_ID_HEADER_NAME;
import static com.crm.sharedlib.consts.CrmConstants.USER_ID_HEADER_NAME;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@Sql(scripts = "classpath:sql/insertTestOrganizations.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:sql/deleteTestOrganization.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class OrganizationUsersControllerTest extends BaseIntegrationTest {

    private final static String BASE_URI = "/api/organizations";

    @MockitoBean
    private AuthClient authClient;

    @Test
    @DisplayName("Get users of organization when user not in organization expected forbidden response")
    public void filterOrganizationWhenUserNotInOrganizationUsersExpectedForbidden() {
        final long organizationId = 100;

        given()
                .contentType(ContentType.JSON)
                .header(USER_ID_HEADER_NAME, 100)
                .header(ORGANIZATION_ID_HEADER_NAME, 100)
                .queryParam("page", 0)
                .queryParam("size", 5)
                .when()
                .get(BASE_URI + "/{organizationId}/users/filter", organizationId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .body("message", is("User is not in the organization"));
    }

    @Test
    @DisplayName("Get users of organization when organization id is not specified expected forbidden response")
    public void filterOrganizationWhenOrganizationIdIsNotSpecifiedUsersExpectedForbidden() {
        final long organizationId = 100;

        given()
                .contentType(ContentType.JSON)
                .header(USER_ID_HEADER_NAME, 100)
                .queryParam("page", 0)
                .queryParam("size", 5)
                .when()
                .get(BASE_URI + "/{organizationId}/users/filter", organizationId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .body("message", is("User is not in the organization"));
    }


    @Test
    @DisplayName("Get users of organization expected success response")
    public void filterOrganizationUsersExpectedSuccess() throws JsonProcessingException {
        final long organizationId = 100;

        final String responseString = """
                    {
                        "content": [
                            {
                                "id": 102,
                                "login": "Test_User",
                                "email": "email@gmail.com",
                                "fullName": "Max Payne",
                                "firstName": "Max",
                                "lastName": "Payne",
                                "createdAt": "2025-05-24T13:43:46.660018Z",
                                "updateAt": "2025-05-24T13:43:46.660018Z",
                                "roles": null
                            }
                        ],
                        "page": {
                            "size": 5,
                            "number": 0,
                            "totalElements": 3,
                            "totalPages": 1
                        }
                    }
                """;

        RestResponsePage response = objectMapper.readValue(responseString, RestResponsePage.class);
        Mockito.when(authClient.filterUsers(Mockito.any()))
                .thenReturn(response);

        given()
                .contentType(ContentType.JSON)
                .header(USER_ID_HEADER_NAME, 1)
                .header(ORGANIZATION_ID_HEADER_NAME, 100)
                .queryParam("page", 0)
                .queryParam("size", 5)
                .when()
                .get(BASE_URI + "/{organizationId}/users/filter", organizationId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("content", hasSize(1))
                .body("page.size", is(5))
                .body("page.number", is(0))
                .body("page.totalElements", is(1))
                .body("page.totalPages", is(1));
    }

    @Test
    @DisplayName("Get users of organization when roles id is specified expected success response with empty content")
    public void filterOrganizationUsersWhenRolesIdIsSpecifiedExpectedSuccess() throws JsonProcessingException {
        final long organizationId = 100;

        final String responseString = """
                    {
                        "content": [],
                        "page": {
                            "size": 5,
                            "number": 0,
                            "totalElements": 0,
                            "totalPages": 0
                        }
                    }
                """;

        RestResponsePage response = objectMapper.readValue(responseString, RestResponsePage.class);
        Mockito.when(authClient.filterUsers(Mockito.any()))
                .thenReturn(response);

        given()
                .contentType(ContentType.JSON)
                .header(USER_ID_HEADER_NAME, 1)
                .header(ORGANIZATION_ID_HEADER_NAME, 100)
                .queryParam("page", 0)
                .queryParam("size", 5)
                .queryParam("rolesId", List.of(100L, 101L))
                .when()
                .get(BASE_URI + "/{organizationId}/users/filter", organizationId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("page.size", is(5))
                .body("page.number", is(0))
                .body("page.totalElements", is(0))
                .body("page.totalPages", is(0));
    }

    @Test
    @DisplayName("Remove user from organization expected success response")
    public void removeUserFromOrganizationExpectedSuccess() {
        final Long userId = 3L;
        final Long organizationId = 100L;

        given()
                .contentType(ContentType.JSON)
                .header(USER_ID_HEADER_NAME, 1)
                .header(ORGANIZATION_ID_HEADER_NAME, 100)
                .when()
                .delete(BASE_URI + "/{organizationId}/users/{userId}", organizationId, userId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value());

    }

}