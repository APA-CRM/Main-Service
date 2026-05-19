package com.crm.main.controller;

import com.crm.main.BaseIntegrationTest;
import com.crm.main.dto.request.TaskPriorityRequest;
import com.crm.main.persistance.entity.TaskPriority;
import com.crm.main.persistance.repository.TaskPriorityRepository;
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
        "classpath:sql/insertTestTaskPriorities.sql"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {
        "classpath:sql/deleteTestTaskPriorities.sql",
        "classpath:sql/deleteTestOrganization.sql"
}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class TaskPriorityControllerTest extends BaseIntegrationTest {

    private final static String BASE_URI = "/api/organizations";

    @Autowired
    private TaskPriorityRepository priorityRepository;

    @Test
    @DisplayName("Get task priorities of an organization expected success")
    public void getTaskPrioritiesByOrganizationExpectedSuccess() {

        final long organizationId = 100L;

        given()
                .contentType(ContentType.JSON)
                .header(USER_ID_HEADER_NAME, "1")
                .header(ORGANIZATION_ID_HEADER_NAME, organizationId)
                .header(USER_PERMISSIONS_HEADER_NAME, "ALL:ALL;")
                .when()
                .get(BASE_URI + "/{organizationId}/tasks/priorities", organizationId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("id", everyItem(notNullValue()))
                .body("name", everyItem(notNullValue()))
                .body("color", everyItem(notNullValue()))
                .body("createdAt", everyItem(notNullValue()))
                .body("updatedAt", everyItem(notNullValue()));
    }

    @Test
    @DisplayName("Get task priority expected success")
    public void getTaskPriorityExpectedSuccess() {

        final Long organizationId = 100L, priorityId = 100L;

        given()
                .contentType(ContentType.JSON)
                .header(USER_ID_HEADER_NAME, "1")
                .header(ORGANIZATION_ID_HEADER_NAME, organizationId)
                .header(USER_PERMISSIONS_HEADER_NAME, "ALL:ALL;")
                .when()
                .get(BASE_URI + "/{organizationId}/tasks/priorities/{priorityId}", organizationId, priorityId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("id", is(priorityId.intValue()))
                .body("name", notNullValue())
                .body("color", notNullValue())
                .body("createdAt", notNullValue())
                .body("updatedAt", notNullValue());
    }

    @Test
    @DisplayName("Get task priority when not exists expected not found")
    public void getTaskPriorityWhenNotExistsExpectedNotFound() {

        final Long organizationId = 100L, priorityId = 5L;

        given()
                .contentType(ContentType.JSON)
                .header(USER_ID_HEADER_NAME, "1")
                .header(ORGANIZATION_ID_HEADER_NAME, organizationId)
                .header(USER_PERMISSIONS_HEADER_NAME, "ALL:ALL;")
                .when()
                .get(BASE_URI + "/{organizationId}/tasks/priorities/{priorityId}", organizationId, priorityId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("message", is("Task priority is not found"));
    }

    @Test
    @DisplayName("Create task priority expected success")
    public void createTaskPriorityExpectedSuccess() {
        final Long organizationId = 100L;

        TaskPriorityRequest request = new TaskPriorityRequest();
        request.setName("Urgently");
        request.setColor("#00FF00");

        given()
                .contentType(ContentType.JSON)
                .header(USER_ID_HEADER_NAME, "1")
                .header(ORGANIZATION_ID_HEADER_NAME, organizationId)
                .header(USER_PERMISSIONS_HEADER_NAME, "ALL:ALL;")
                .body(request)
                .when()
                .post(BASE_URI + "/{organizationId}/tasks/priorities", organizationId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", notNullValue())
                .body("name", is(request.getName()))
                .body("color", is(request.getColor()))
                .body("createdAt", notNullValue())
                .body("updatedAt", notNullValue());
    }

    @Test
    @DisplayName("Update task priority expected success")
    public void updateTaskPriorityExpectedSuccess() {
        final Long organizationId = 100L, priorityId = 100L;

        TaskPriorityRequest request = new TaskPriorityRequest();
        request.setName("Urgently");
        request.setColor("#00FF00");

        given()
                .contentType(ContentType.JSON)
                .header(USER_ID_HEADER_NAME, "1")
                .header(ORGANIZATION_ID_HEADER_NAME, organizationId)
                .header(USER_PERMISSIONS_HEADER_NAME, "ALL:ALL;")
                .body(request)
                .when()
                .put(BASE_URI + "/{organizationId}/tasks/priorities/{priorityId}", organizationId, priorityId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("id", is(priorityId.intValue()))
                .body("name", is(request.getName()))
                .body("color", is(request.getColor()))
                .body("createdAt", notNullValue())
                .body("updatedAt", notNullValue());
    }


    @Test
    @DisplayName("Delete task priority expected success")
    public void deleteTaskPriorityExpectedSuccess() {
        final Long organizationId = 100L, priorityId = 100L;

        given()
                .contentType(ContentType.JSON)
                .header(USER_ID_HEADER_NAME, "1")
                .header(ORGANIZATION_ID_HEADER_NAME, organizationId)
                .header(USER_PERMISSIONS_HEADER_NAME, "ALL:ALL;")
                .when()
                .delete(BASE_URI + "/{organizationId}/tasks/priorities/{priorityId}", organizationId, priorityId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value());

        Optional<TaskPriority> taskPriority = priorityRepository.findById(priorityId);

        assertTrue(taskPriority.isEmpty());
    }

    @Test
    @DisplayName("Delete task priority when a task use it expected conflict")
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
    public void deleteTaskPriorityWhenTaskUseItExpectedConflict() {
        final Long organizationId = 100L, priorityId = 101L;

        given()
                .contentType(ContentType.JSON)
                .header(USER_ID_HEADER_NAME, "1")
                .header(ORGANIZATION_ID_HEADER_NAME, organizationId)
                .header(USER_PERMISSIONS_HEADER_NAME, "ALL:ALL;")
                .when()
                .delete(BASE_URI + "/{organizationId}/tasks/priorities/{priorityId}", organizationId, priorityId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.CONFLICT.value())
                .body("message", is("Cannot delete this priority because it’s used by existing tasks"));
    }

}