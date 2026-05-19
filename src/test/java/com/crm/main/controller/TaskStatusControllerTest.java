package com.crm.main.controller;

import com.crm.main.BaseIntegrationTest;
import com.crm.main.dto.request.TaskStatusRequest;
import com.crm.main.enums.TaskStatusType;
import com.crm.main.persistance.entity.TaskStatus;
import com.crm.main.persistance.repository.TaskStatusRepository;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static com.crm.sharedlib.core.consts.CrmHeaders.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Sql(scripts = {
        "classpath:sql/insertTestOrganizations.sql",
        "classpath:sql/insertTestTaskStatuses.sql"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {
        "classpath:sql/deleteTestTaskStatuses.sql",
        "classpath:sql/deleteTestOrganization.sql"
}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class TaskStatusControllerTest extends BaseIntegrationTest {

    private final static String BASE_URI = "/api/organizations";

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Test
    @DisplayName("Get task statuses of an organization expected success")
    public void getTaskStatusesByOrganizationExpectedSuccess() {

        final long organizationId = 100L;

        given()
                .contentType(ContentType.JSON)
                .header(USER_ID_HEADER_NAME, "1")
                .header(ORGANIZATION_ID_HEADER_NAME, organizationId)
                .header(USER_PERMISSIONS_HEADER_NAME, "ALL:ALL;")
                .when()
                .get(BASE_URI + "/{organizationId}/tasks/statuses", organizationId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("id", everyItem(notNullValue()))
                .body("name", everyItem(notNullValue()))
                .body("color", everyItem(notNullValue()))
                .body("type", everyItem(notNullValue()))
                .body("createdAt", everyItem(notNullValue()))
                .body("updatedAt", everyItem(notNullValue()));
    }

    @Test
    @DisplayName("Get task status expected success")
    public void getTaskStatusExpectedSuccess() {

        final Long organizationId = 100L, statusId = 100L;

        given()
                .contentType(ContentType.JSON)
                .header(USER_ID_HEADER_NAME, "1")
                .header(ORGANIZATION_ID_HEADER_NAME, organizationId)
                .header(USER_PERMISSIONS_HEADER_NAME, "ALL:ALL;")
                .when()
                .get(BASE_URI + "/{organizationId}/tasks/statuses/{statusId}", organizationId, statusId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("id", is(statusId.intValue()))
                .body("name", notNullValue())
                .body("color", notNullValue())
                .body("type", notNullValue())
                .body("createdAt", notNullValue())
                .body("updatedAt", notNullValue());
    }

    @Test
    @DisplayName("Get task status when not exists expected not found")
    public void getTaskStatusWhenNotExistsExpectedNotFound() {

        final Long organizationId = 100L, statusId = 5L;

        given()
                .contentType(ContentType.JSON)
                .header(USER_ID_HEADER_NAME, "1")
                .header(ORGANIZATION_ID_HEADER_NAME, organizationId)
                .header(USER_PERMISSIONS_HEADER_NAME, "ALL:ALL;")
                .when()
                .get(BASE_URI + "/{organizationId}/tasks/statuses/{statusId}", organizationId, statusId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("message", is("Task status is not found"));
    }

    @Test
    @DisplayName("Create task status expected success")
    public void createTaskStatusExpectedSuccess() {
        final Long organizationId = 100L;

        TaskStatusRequest request = new TaskStatusRequest();
        request.setName("To Do");
        request.setColor("#00FF00");
        request.setType(TaskStatusType.TODO);

        given()
                .contentType(ContentType.JSON)
                .header(USER_ID_HEADER_NAME, "1")
                .header(ORGANIZATION_ID_HEADER_NAME, organizationId)
                .header(USER_PERMISSIONS_HEADER_NAME, "ALL:ALL;")
                .body(request)
                .when()
                .post(BASE_URI + "/{organizationId}/tasks/statuses", organizationId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", notNullValue())
                .body("name", is(request.getName()))
                .body("color", is(request.getColor()))
                .body("type", is(request.getType().name()))
                .body("createdAt", notNullValue())
                .body("updatedAt", notNullValue());
    }

    @Test
    @DisplayName("Update task status expected success")
    public void updateTaskStatusExpectedSuccess() {
        final Long organizationId = 100L, statusId = 100L;

        TaskStatusRequest request = new TaskStatusRequest();
        request.setName("Blocked");
        request.setColor("#00FF00");
        request.setType(TaskStatusType.BLOCKED);

        given()
                .contentType(ContentType.JSON)
                .header(USER_ID_HEADER_NAME, "1")
                .header(ORGANIZATION_ID_HEADER_NAME, organizationId)
                .header(USER_PERMISSIONS_HEADER_NAME, "ALL:ALL;")
                .body(request)
                .when()
                .put(BASE_URI + "/{organizationId}/tasks/statuses/{statusId}", organizationId, statusId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("id", is(statusId.intValue()))
                .body("name", is(request.getName()))
                .body("color", is(request.getColor()))
                .body("type", is(request.getType().name()))
                .body("createdAt", notNullValue())
                .body("updatedAt", notNullValue());
    }


    @Test
    @DisplayName("Delete task status expected success")
    public void deleteTaskStatusExpectedSuccess() {
        final Long organizationId = 100L, statusId = 100L;

        given()
                .contentType(ContentType.JSON)
                .header(USER_ID_HEADER_NAME, "1")
                .header(ORGANIZATION_ID_HEADER_NAME, organizationId)
                .header(USER_PERMISSIONS_HEADER_NAME, "ALL:ALL;")
                .when()
                .delete(BASE_URI + "/{organizationId}/tasks/statuses/{statusId}", organizationId, statusId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value());

        Optional<TaskStatus> taskStatus = taskStatusRepository.findById(statusId);

        assertTrue(taskStatus.isEmpty());
    }

    @Test
    @DisplayName("Delete task status when a task use it expected conflict")
    @Sql(scripts = {
            "classpath:sql/insertTestOrganizations.sql",
            "classpath:sql/insertTestTaskPriorities.sql",
            "classpath:sql/insertTestTaskStatuses.sql",
            "classpath:sql/insertTestTasks.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:sql/deleteTestTasks.sql",
            "classpath:sql/deleteTestTaskPriorities.sql",
            "classpath:sql/deleteTestTaskStatuses.sql",
            "classpath:sql/deleteTestOrganization.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteTaskStatusWhenTaskUseItExpectedConflict() {
        final Long organizationId = 100L, statusId = 100L;

        given()
                .contentType(ContentType.JSON)
                .header(USER_ID_HEADER_NAME, "1")
                .header(ORGANIZATION_ID_HEADER_NAME, organizationId)
                .header(USER_PERMISSIONS_HEADER_NAME, "ALL:ALL;")
                .when()
                .delete(BASE_URI + "/{organizationId}/tasks/statuses/{statusId}", organizationId, statusId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.CONFLICT.value())
                .body("message", is("Cannot delete this status because it’s used by existing tasks"));
    }


}