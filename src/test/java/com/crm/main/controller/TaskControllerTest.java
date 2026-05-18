package com.crm.main.controller;

import com.crm.main.BaseIntegrationTest;
import com.crm.main.dto.request.TaskRequest;
import com.crm.main.persistance.entity.Task;
import com.crm.main.persistance.repository.TaskRepository;
import com.crm.sharedlib.messaging.service.MessagingService;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;
import java.util.UUID;

import static com.crm.sharedlib.core.consts.CrmHeaders.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

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
class TaskControllerTest extends BaseIntegrationTest {

    private final static String BASE_URI = "/api/organizations";

    @Autowired
    private TaskRepository taskRepository;

    @MockitoBean
    private MessagingService messagingService;

    @Test
    @DisplayName("Get task expected success")
    public void getTaskExpectedSuccess() {

        final String taskId = "00000000-0000-0000-0000-000000000001";
        final Long organizationId = 100L;

        given()
                .contentType(ContentType.JSON)
                .header(USER_ID_HEADER_NAME, "1")
                .header(ORGANIZATION_ID_HEADER_NAME, organizationId)
                .header(USER_PERMISSIONS_HEADER_NAME, "ALL:ALL;")
                .when()
                .get(BASE_URI + "/{organizationId}/tasks/{taskId}", organizationId, taskId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("id", is(taskId))
                .body("title", notNullValue())
                .body("description", notNullValue())
                .body("estimatedTime", notNullValue())
                .body("status", notNullValue())
                .body("priority", notNullValue())
                .body("assignedTo", notNullValue())
                .body("createdBy", notNullValue())
                .body("dueDate", notNullValue())
                .body("reminderAt", notNullValue())
                .body("createdAt", notNullValue())
                .body("updatedAt", notNullValue());
    }

    @Test
    @DisplayName("Filter tasks when status is specified expected only one organization's tasks")
    public void filterTasksWhenStatusSpecifiedExpectedSuccess() {
        final Long organizationId = 100L, statusId = 100L;

        given()
                .contentType(ContentType.JSON)
                .header(USER_ID_HEADER_NAME, "1")
                .header(ORGANIZATION_ID_HEADER_NAME, organizationId)
                .header(USER_PERMISSIONS_HEADER_NAME, "ALL:ALL;")
                .queryParam("page", 0)
                .queryParam("size", 10)
                .queryParam("statusId", statusId)
                .when()
                .get(BASE_URI + "/{organizationId}/tasks/filter", organizationId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("content", hasSize(2))
                .body("content.status.id", everyItem(is(statusId.intValue())))
                .body("content.organizationId", everyItem(is(organizationId.intValue())))
                .body("page.totalElements", is(2));
    }

    @Test
    @DisplayName("Filter tasks expected only one organization's tasks")
    public void filterTasksExpectedOnly() {
        final Long organizationId = 100L;

        given()
                .contentType(ContentType.JSON)
                .header(USER_ID_HEADER_NAME, "1")
                .header(ORGANIZATION_ID_HEADER_NAME, organizationId)
                .header(USER_PERMISSIONS_HEADER_NAME, "ALL:ALL;")
                .queryParam("page", 0)
                .queryParam("size", 10)
                .when()
                .get(BASE_URI + "/{organizationId}/tasks/filter", organizationId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("content", hasSize(3))
                .body("content.organizationId", everyItem(is(organizationId.intValue())))
                .body("page.totalElements", is(3));
    }

    @Test
    @DisplayName("Create task expected success")
    public void createTaskExpectedSuccess() {

        final Long organizationId = 100L, userId = 1L;

        TaskRequest request = new TaskRequest();
        request.setAssignedTo(100L);
        request.setTitle("New task");
        request.setDescription("Task to test creating of the task");
        request.setStatusId(100L);
        request.setPriorityId(100L);
        request.setEstimatedTime(4);

        given()
                .contentType(ContentType.JSON)
                .header(USER_ID_HEADER_NAME, userId)
                .header(ORGANIZATION_ID_HEADER_NAME, organizationId)
                .header(USER_PERMISSIONS_HEADER_NAME, "ALL:ALL;")
                .body(request)
                .when()
                .post(BASE_URI + "/{organizationId}/tasks", organizationId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("id", notNullValue())
                .body("title", is(request.getTitle()))
                .body("description", is(request.getDescription()))
                .body("estimatedTime", is(request.getEstimatedTime()))
                .body("status.id", is(request.getStatusId().intValue()))
                .body("priority.id", is(request.getPriorityId().intValue()))
                .body("assignedTo", is(request.getAssignedTo().intValue()))
                .body("createdBy", is(1))
                .body("dueDate", nullValue())
                .body("reminderAt", nullValue())
                .body("createdAt", notNullValue())
                .body("updatedAt", notNullValue());

        // Verify that message about assigned has been sent to the user
        Mockito.verify(messagingService, Mockito.atLeastOnce())
                .sendMessageToUser(eq(request.getAssignedTo()), any(), any(), any(), any());
    }

    @Test
    @DisplayName("Update task expected success")
    public void updateTaskExpectedSuccess() {

        final String taskId = "00000000-0000-0000-0000-000000000003";
        final Long organizationId = 100L;

        TaskRequest request = new TaskRequest();
        request.setAssignedTo(102L);
        request.setTitle("Update");
        request.setDescription("Task to test updating of the task");
        request.setStatusId(102L);
        request.setPriorityId(102L);
        request.setEstimatedTime(4);

        given()
                .contentType(ContentType.JSON)
                .header(USER_ID_HEADER_NAME, "1")
                .header(ORGANIZATION_ID_HEADER_NAME, organizationId)
                .header(USER_PERMISSIONS_HEADER_NAME, "ALL:ALL;")
                .body(request)
                .when()
                .put(BASE_URI + "/{organizationId}/tasks/{taskId}", organizationId, taskId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("id", is(taskId))
                .body("title", is(request.getTitle()))
                .body("description", is(request.getDescription()))
                .body("estimatedTime", is(request.getEstimatedTime()))
                .body("status.id", is(request.getStatusId().intValue()))
                .body("priority.id", is(request.getPriorityId().intValue()))
                .body("assignedTo", is(request.getAssignedTo().intValue()))
                .body("createdBy", is(101))
                .body("dueDate", nullValue())
                .body("reminderAt", nullValue())
                .body("completedAt", notNullValue())
                .body("createdAt", notNullValue())
                .body("updatedAt", notNullValue());

        // Verify that message about assigned has been sent to the user
        Mockito.verify(messagingService, Mockito.atLeastOnce())
                .sendMessageToUser(eq(request.getAssignedTo()), any(), any(), any(), any());
    }

    @Test
    @DisplayName("Delete task expected success")
    public void deleteTaskExpectedSuccess() {

        final String taskId = "00000000-0000-0000-0000-000000000002";
        final Long organizationId = 100L;

        given()
                .contentType(ContentType.JSON)
                .header(USER_ID_HEADER_NAME, "1")
                .header(ORGANIZATION_ID_HEADER_NAME, organizationId)
                .header(USER_PERMISSIONS_HEADER_NAME, "ALL:ALL;")
                .when()
                .delete(BASE_URI + "/{organizationId}/tasks/{taskId}", organizationId, taskId)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value());

        Optional<Task> task = taskRepository.findById(UUID.fromString(taskId));

        assertTrue(task.isEmpty());
    }

}