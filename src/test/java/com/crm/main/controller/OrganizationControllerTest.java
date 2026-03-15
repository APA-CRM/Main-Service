package com.crm.main.controller;

import com.crm.main.BaseIntegrationTest;
import com.crm.main.dto.request.OrganizationRequest;
import com.crm.main.feign.AuthClient;
import com.crm.main.feign.FileClient;
import com.crm.sharedlib.core.dto.response.FileIdResponse;
import com.crm.sharedlib.core.dto.response.RoleResponse;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;

import java.util.UUID;

import static com.crm.sharedlib.core.consts.CrmHeaders.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@Sql(scripts = "classpath:sql/insertTestOrganizations.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {
        "classpath:sql/deleteTestOrganizationFile.sql",
        "classpath:sql/deleteTestOrganization.sql"
}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class OrganizationControllerTest extends BaseIntegrationTest {

    private final static String BASE_URI = "/api/organizations";

    @MockitoBean
    private AuthClient authClient;

    @MockitoBean
    private FileClient fileClient;

    @MockitoBean
    private RabbitTemplate rabbitTemplate;

    @Test
    @DisplayName("Create organization expected success")
    public void createOrganizationExpectedSuccess() {

        OrganizationRequest request = new OrganizationRequest();

        request.setEmail("test@gmail.com");
        request.setCity("city");
        request.setName("Name of organization");
        request.setCountry("country");
        request.setAddress("address");
        request.setDescription("JavaRush");

        RoleResponse response = new RoleResponse();

        response.setId(1L);

        Mockito.when(authClient.createRole(Mockito.any()))
                .thenReturn(response);

        Mockito.when(fileClient.createDefaultDirectory(Mockito.any()))
                .thenReturn(new FileIdResponse(UUID.randomUUID()));

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .header(USER_ID_HEADER_NAME, "101")
                .when()
                .post(BASE_URI)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("id", notNullValue())
                .body("name", is(request.getName()))
                .body("address", is(request.getAddress()))
                .body("city", is(request.getCity()))
                .body("country", is(request.getCountry()))
                .body("email", is(request.getEmail()))
                .body("description", is(request.getDescription()))
                .body("createdAt", notNullValue())
                .body("updatedAt", notNullValue());


    }

    @Test
    @DisplayName("Get organization of user expected success")
    public void getOrganizationOfUserExpectedSuccess() {
        given()
                .contentType(ContentType.JSON)
                .header(USER_ID_HEADER_NAME, "1")
                .when()
                .get(BASE_URI)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("[0].id", notNullValue())
                .body("[0].name", notNullValue());
    }

    @Test
    @DisplayName("Get organization preview expected success")
    public void getOrganizationPreviewExpectedSuccess() {
        final int organizationId = 100;

        given()
                .contentType(ContentType.JSON)
                .when()
                .get(BASE_URI + "/{organizationId}/preview", organizationId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("id", is(organizationId))
                .body("name", notNullValue());
    }

    @Test
    @DisplayName("Get organization preview when organization is not exists expected not found response")
    public void getOrganizationPreviewWhenNotFoundExpectedNotFound() {
        final int organizationId = 200;

        given()
                .contentType(ContentType.JSON)
                .when()
                .get(BASE_URI + "/{organizationId}/preview", organizationId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("message", is("Organization is not found"));
    }

    @Test
    @DisplayName("Get organization by ID expected success")
    public void getOrganizationById() {
        final int organizationId = 100;

        given()
                .contentType(ContentType.JSON)
                .header(USER_ID_HEADER_NAME, "101")
                .when()
                .get(BASE_URI + "/{organizationId}", organizationId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("id", notNullValue())
                .body("name", notNullValue())
                .body("address", notNullValue())
                .body("city", notNullValue())
                .body("country", notNullValue())
                .body("email", notNullValue())
                .body("description", notNullValue());
    }

    @Test
    @DisplayName("Update organization by ID expected success")
    public void updateOrganizationById() {
        final int organizationId = 100;
        OrganizationRequest request = new OrganizationRequest();

        request.setEmail("test@gmail.com");
        request.setCity("city");
        request.setName("Jopi4i");
        request.setCountry("KIEV");
        request.setAddress("address");
        request.setDescription("JavaRush");

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .header(USER_ID_HEADER_NAME, "1")
                .header(ORGANIZATION_ID_HEADER_NAME, organizationId)
                .header(USER_PERMISSIONS_HEADER_NAME, "ALL:ALL;")
                .when()
                .put(BASE_URI + "/{organizationId}", organizationId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("id", notNullValue())
                .body("name", is(request.getName()))
                .body("address", is(request.getAddress()))
                .body("city", is(request.getCity()))
                .body("country", is(request.getCountry()))
                .body("email", is(request.getEmail()))
                .body("description", is(request.getDescription()))
                .body("updatedAt", notNullValue());
    }

}