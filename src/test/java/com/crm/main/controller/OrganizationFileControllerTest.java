package com.crm.main.controller;

import com.crm.main.BaseIntegrationTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import java.util.UUID;

import static com.crm.sharedlib.core.consts.CrmHeaders.ORGANIZATION_ID_HEADER_NAME;
import static com.crm.sharedlib.core.consts.CrmHeaders.USER_ID_HEADER_NAME;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@Sql(scripts = {
        "classpath:sql/insertTestOrganizations.sql",
        "classpath:sql/insertTestOrganizationFile.sql"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {
        "classpath:sql/deleteTestOrganizationFile.sql",
        "classpath:sql/deleteTestOrganization.sql"
}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class OrganizationFileControllerTest extends BaseIntegrationTest {

    private final static String BASE_URI = "/api/organizations";

    @Test
    @DisplayName("Get organization file expected success")
    public void getOrganizationFileExpectedSuccess() {
        final int organizationId = 100;
        final UUID fileId = UUID.fromString("5682d1e7-3eb4-4e41-923a-7b7abc0239c1");

        given()
                .contentType(ContentType.JSON)
                .header(USER_ID_HEADER_NAME, 1)
                .header(ORGANIZATION_ID_HEADER_NAME, 100)
                .when()
                .get(BASE_URI + "/{organizationId}/files/{fileId}", organizationId, fileId)
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .assertThat()
                .body("id", notNullValue())
                .body("organization", notNullValue())
                .body("organization.id", is(organizationId))
                .body("fileId", is(fileId.toString()))
                .body("updatedAt", notNullValue())
                .body("createdAt", notNullValue());

    }

    @Test
    @DisplayName("Get root organization file expected success")
    public void getRootOrganizationFileExpectedSuccess() {
        final int organizationId = 100;

        given()
                .contentType(ContentType.JSON)
                .header(USER_ID_HEADER_NAME, 1)
                .header(ORGANIZATION_ID_HEADER_NAME, 100)
                .when()
                .get(BASE_URI + "/{organizationId}/files/root", organizationId)
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .assertThat()
                .body("id", notNullValue())
                .body("organization", notNullValue())
                .body("organization.id", is(organizationId))
                .body("fileId", notNullValue())
                .body("updatedAt", notNullValue())
                .body("createdAt", notNullValue());

    }

    @Test
    @DisplayName("Get organization file when not exists expected not found")
    public void createOrganizationFileWhenNotExistsExpectedNotFound() {
        final int organizationId = 100;
        final UUID fileId = UUID.fromString("5682d1e7-3eb4-4e41-923a-7b7abc023333");

        given()
                .contentType(ContentType.JSON)
                .header(USER_ID_HEADER_NAME, 1)
                .header(ORGANIZATION_ID_HEADER_NAME, 100)
                .when()
                .get(BASE_URI + "/{organizationId}/files/{fileId}", organizationId, fileId)
                .then()
                .log().all()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .assertThat()
                .body("message", is("Organization file is not found"));

    }

    @Test
    @DisplayName("Create organization file expected success")
    public void createOrganizationFileExpectedSuccess() {
        final int organizationId = 100;
        final UUID fileId = UUID.fromString("5682d1e7-3eb4-4e41-923a-7b7abc0239cc");

        given()
                .contentType(ContentType.JSON)
                .header(USER_ID_HEADER_NAME, 1)
                .header(ORGANIZATION_ID_HEADER_NAME, 100)
                .when()
                .post(BASE_URI + "/{organizationId}/files/{fileId}", organizationId, fileId)
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .assertThat()
                .body("id", notNullValue())
                .body("organization", notNullValue())
                .body("organization.id", is(organizationId))
                .body("fileId", is(fileId.toString()))
                .body("updatedAt", notNullValue())
                .body("createdAt", notNullValue());

    }

    @Test
    @DisplayName("Create organization file when already exists expected conflict")
    public void createOrganizationFileWhenAlreadyExistsExpectedConflict() {
        final int organizationId = 100;
        final UUID fileId = UUID.fromString("5682d1e7-3eb4-4e41-923a-7b7abc0239c1");

        given()
                .contentType(ContentType.JSON)
                .header(USER_ID_HEADER_NAME, 1)
                .header(ORGANIZATION_ID_HEADER_NAME, 100)
                .when()
                .post(BASE_URI + "/{organizationId}/files/{fileId}", organizationId, fileId)
                .then()
                .log().all()
                .statusCode(HttpStatus.CONFLICT.value())
                .assertThat()
                .body("message", is("File already in the organization"));
    }

    @Test
    @DisplayName("Delete organization file expected success")
    public void deleteOrganizationFileExpectedSuccess() {
        final int organizationId = 100;
        final UUID fileId = UUID.fromString("5682d1e7-3eb4-4e41-923a-7b7abc0239c1");

        given()
                .contentType(ContentType.JSON)
                .header(USER_ID_HEADER_NAME, 1)
                .header(ORGANIZATION_ID_HEADER_NAME, 100)
                .when()
                .delete(BASE_URI + "/{organizationId}/files/{fileId}", organizationId, fileId)
                .then()
                .log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

}