package com.crm.main.controller.internal;

import com.crm.main.BaseIntegrationTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import java.util.UUID;

import static com.crm.sharedlib.consts.CrmConstants.ORGANIZATION_ID_HEADER_NAME;
import static com.crm.sharedlib.consts.CrmConstants.USER_ID_HEADER_NAME;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@Sql(scripts = {
        "classpath:sql/insertTestOrganizations.sql",
        "classpath:sql/insertTestOrganizationFile.sql"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {
        "classpath:sql/deleteTestOrganizationFile.sql",
        "classpath:sql/deleteTestOrganization.sql"
}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class InternalOrganizationFilesControllerTest extends BaseIntegrationTest {

    private final static String BASE_URI = "/api/internal/organizations";

    @Test
    @DisplayName("Check if organization has a file expected success")
    public void checkIfOrganizationHasFileExpectedSuccess() {
        final int organizationId = 100;
        final UUID fileId = UUID.fromString("5682d1e7-3eb4-4e41-923a-7b7abc0239c1");

        given()
                .contentType(ContentType.JSON)
                .header(USER_ID_HEADER_NAME, 1)
                .header(ORGANIZATION_ID_HEADER_NAME, organizationId)
                .when()
                .get(BASE_URI + "/{organizationId}/files/{fileId}/check", organizationId, fileId)
                .then()
                .log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("Check if organization has a file when user is not in organization expected forbidden")
    public void checkIfOrganizationHasFileWhenUserIsNotInOrganizationExpectedForbidden() {
        final int organizationId = 100;
        final UUID fileId = UUID.fromString("5682d1e7-3eb4-4e41-923a-7b7abc0239c1");

        given()
                .contentType(ContentType.JSON)
                .header(USER_ID_HEADER_NAME, 100)
                .header(ORGANIZATION_ID_HEADER_NAME, organizationId)
                .when()
                .get(BASE_URI + "/{organizationId}/files/{fileId}/check", organizationId, fileId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .body("message", is("User is not in the organization"));
    }

}