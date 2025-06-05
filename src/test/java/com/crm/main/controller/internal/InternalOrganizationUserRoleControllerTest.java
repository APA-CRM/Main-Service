package com.crm.main.controller.internal;

import com.crm.main.controller.BaseIntegrationTest;
import com.crm.sharedlib.consts.CrmConstants;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;

@Sql(scripts = "classpath:sql/insertTestOrganizations.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:sql/deleteTestOrganization.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class InternalOrganizationUserRoleControllerTest extends BaseIntegrationTest {

    private final static String BASE_URI = "/api/internal/organizations";

    @Test
    @DisplayName("Get organization user roles expected success response")
    public void getOrganizationUserRolesExpectedSuccess() {

        final int organizationId = 100;
        final int userId = 1;

        given()
                .contentType(ContentType.JSON)
                .header(CrmConstants.USER_ID_HEADER_NAME, "1")
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

}