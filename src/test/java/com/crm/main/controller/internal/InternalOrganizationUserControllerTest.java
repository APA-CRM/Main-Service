package com.crm.main.controller.internal;

import com.crm.main.BaseIntegrationTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import static com.crm.sharedlib.core.consts.CrmHeaders.USER_ID_HEADER_NAME;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;

@Sql(scripts = "classpath:sql/insertTestOrganizations.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:sql/deleteTestOrganization.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class InternalOrganizationUserControllerTest extends BaseIntegrationTest {

    private final static String BASE_URI = "/api/internal/organizations";

    @Test
    @DisplayName("Get organization user expected success response")
    public void getOrganizationUserExpectedSuccess() {

        final int organizationId = 100;
        final int userId = 1;

        given()
                .contentType(ContentType.JSON)
                .header(USER_ID_HEADER_NAME, "1")
                .when()
                .get(BASE_URI + "/{organizationId}/users/{userId}", organizationId, userId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("organizationId", is(organizationId))
                .body("userId", is(userId))
                .body("rolesId", containsInAnyOrder(1));

    }

    @Test
    @DisplayName("Get is user exists in an organization expected success response")
    public void getIsUserExistsInOrganizationExpectedSuccess() {

        final int organizationId = 100;
        final int userId = 1;

        given()
                .contentType(ContentType.JSON)
                .header(USER_ID_HEADER_NAME, "1")
                .when()
                .get(BASE_URI + "/{organizationId}/users/{userId}/exists", organizationId, userId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("isUserExistsInOrganization", is(true));

    }

}