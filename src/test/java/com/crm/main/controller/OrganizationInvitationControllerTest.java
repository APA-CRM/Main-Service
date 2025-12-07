package com.crm.main.controller;

import com.crm.main.BaseIntegrationTest;
import com.crm.main.dto.request.OrganizationInvitationRequest;
import com.crm.main.enums.InvitationStatus;
import com.crm.main.enums.RoleType;
import com.crm.main.feign.AuthClient;
import com.crm.main.persistance.entity.OrganizationRole;
import com.crm.main.persistance.entity.OrganizationRoleUser;
import com.crm.main.persistance.entity.OrganizationUser;
import com.crm.main.persistance.repository.OrganizationRoleUserRepository;
import com.crm.main.persistance.repository.OrganizationUserRepository;
import com.crm.sharedlib.core.dto.response.UserResponse;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static com.crm.sharedlib.core.consts.CrmHeaders.ORGANIZATION_ID_HEADER_NAME;
import static com.crm.sharedlib.core.consts.CrmHeaders.USER_ID_HEADER_NAME;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@Sql(scripts = {
        "classpath:sql/insertTestOrganizations.sql",
        "classpath:sql/insertTestOrganizationInvitations.sql"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {
        "classpath:sql/deleteTestOrganizationInvitation.sql",
        "classpath:sql/deleteTestOrganization.sql"
}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class OrganizationInvitationControllerTest extends BaseIntegrationTest {

    private final static String BASE_URI = "/api/organizations";

    @MockitoBean
    private RabbitTemplate rabbitTemplate;

    @MockitoBean
    private AuthClient authClient;

    @Autowired
    private OrganizationUserRepository userRepository;

    @Autowired
    private OrganizationRoleUserRepository roleUserRepository;

    @Test
    @DisplayName("Invite user to organization expected success")
    public void inviteUserToOrganizationExpectedSuccess() {
        final int organizationId = 100;

        OrganizationInvitationRequest request = new OrganizationInvitationRequest();
        request.setUserId(5L);

        UserResponse response = new UserResponse();

        response.setEmail("test@gmail.com");

        Mockito.when(authClient.getUserById(Mockito.anyLong()))
                .thenAnswer(invocation -> {
                    Long userId = invocation.getArgument(0);

                    response.setId(userId);

                    return response;
                });

        given()
                .contentType(ContentType.JSON)
                .header(USER_ID_HEADER_NAME, 1)
                .header(ORGANIZATION_ID_HEADER_NAME, 100)
                .body(request)
                .when()
                .post(BASE_URI + "/{organizationId}/invitations", organizationId)
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .assertThat()
                .body("id", notNullValue())
                .body("organization", notNullValue())
                .body("organization.id", is(organizationId))
                .body("userId", is(5))
                .body("status", is(InvitationStatus.PENDING.name()))
                .body("invitorId", is(1))
                .body("roleId", is(2))
                .body("expiredAt", notNullValue())
                .body("createdAt", notNullValue())
                .body("updatedAt", notNullValue());

    }

    @Test
    @DisplayName("Invite user to organization when role id is specified expected success")
    public void inviteUserToOrganizationWhenRoleIdIsSpecifiedExpectedSuccess() {
        final int organizationId = 100;

        OrganizationInvitationRequest request = new OrganizationInvitationRequest();
        request.setUserId(5L);
        request.setRoleId(4L);

        UserResponse response = new UserResponse();

        response.setEmail("test@gmail.com");

        Mockito.when(authClient.getUserById(Mockito.anyLong()))
                .thenAnswer(invocation -> {
                    Long userId = invocation.getArgument(0);

                    response.setId(userId);

                    return response;
                });

        given()
                .contentType(ContentType.JSON)
                .header(USER_ID_HEADER_NAME, 1)
                .header(ORGANIZATION_ID_HEADER_NAME, 100)
                .body(request)
                .when()
                .post(BASE_URI + "/{organizationId}/invitations", organizationId)
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .assertThat()
                .body("id", notNullValue())
                .body("organization", notNullValue())
                .body("organization.id", is(organizationId))
                .body("userId", is(request.getUserId().intValue()))
                .body("status", is(InvitationStatus.PENDING.name()))
                .body("invitorId", is(1))
                .body("roleId", is(request.getRoleId().intValue()))
                .body("expiredAt", notNullValue())
                .body("createdAt", notNullValue())
                .body("updatedAt", notNullValue());

    }

    @Test
    @DisplayName("Invite user to organization when the user already in organization expected conflict")
    public void inviteUserToOrganizationWhenUserAlreadyInOrganizationExpectedConflict() {
        final int organizationId = 100;

        OrganizationInvitationRequest request = new OrganizationInvitationRequest();

        request.setUserId(3L);

        UserResponse response = new UserResponse();

        response.setEmail("test@gmail.com");

        Mockito.when(authClient.getUserById(Mockito.anyLong()))
                .thenAnswer(invocation -> {
                    Long userId = invocation.getArgument(0);

                    response.setId(userId);

                    return response;
                });

        given()
                .contentType(ContentType.JSON)
                .header(USER_ID_HEADER_NAME, 1)
                .header(ORGANIZATION_ID_HEADER_NAME, 100)
                .body(request)
                .when()
                .post(BASE_URI + "/{organizationId}/invitations", organizationId)
                .then()
                .log().all()
                .statusCode(HttpStatus.CONFLICT.value())
                .assertThat()
                .body("message", is("User already in organization"));

    }

    @Test
    @DisplayName("Invite user to organization when invitation already exists expected conflict")
    public void inviteUserToOrganizationWhenInvitationAlreadyExistsExpectedConflict() {
        final int organizationId = 100;

        OrganizationInvitationRequest request = new OrganizationInvitationRequest();
        request.setUserId(10L);

        UserResponse response = new UserResponse();

        response.setEmail("test@gmail.com");

        Mockito.when(authClient.getUserById(Mockito.anyLong()))
                .thenAnswer(invocation -> {
                    Long userId = invocation.getArgument(0);

                    response.setId(userId);

                    return response;
                });

        given()
                .contentType(ContentType.JSON)
                .header(USER_ID_HEADER_NAME, 1)
                .header(ORGANIZATION_ID_HEADER_NAME, 100)
                .body(request)
                .when()
                .post(BASE_URI + "/{organizationId}/invitations", organizationId)
                .then()
                .log().all()
                .statusCode(HttpStatus.CONFLICT.value())
                .assertThat()
                .body("message", is("Invitation is already created"));

    }

    @Test
    @DisplayName("Accept invitation of organization expected success")
    public void acceptInvitationOfOrganizationWhenRoleIsMemberExpectedSuccess() {
        final String invitationId = "6d2718fd-d2b0-4ed2-9074-9bcc2f7c28d1";

        JsonPath jsonPath = given()
                .contentType(ContentType.JSON)
                .header(USER_ID_HEADER_NAME, 10)
                .when()
                .patch(BASE_URI + "/invitations/{invitationId}/accept", invitationId)
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .assertThat()
                .body("id", notNullValue())
                .body("organization", notNullValue())
                .body("userId", is(10))
                .body("status", is(InvitationStatus.ACCEPTED.name()))
                .body("roleId", is(2))
                .body("invitorId", is(1))
                .body("expiredAt", notNullValue())
                .extract().jsonPath();

        long organizationId = jsonPath.getLong("organization.id");


        Optional<OrganizationUser> organizationUser =
                userRepository.findByOrganizationIdAndUserId(organizationId, 10L);

        assertThat(organizationUser).isNotEmpty();

        List<OrganizationRoleUser> userRoles = roleUserRepository.findByOrganizationUser(organizationUser.get());

        assertThat(userRoles)
                .hasSize(1)
                .map(OrganizationRoleUser::getOrganizationRole)
                .map(OrganizationRole::getRoleType)
                .contains(RoleType.MEMBER);
    }

    @Test
    @DisplayName("Accept invitation of organization when invitation is expired expected forbidden")
    public void acceptInvitationWhenInvitationIsExpiredExpectedForbidden() {
        final String invitationId = "6d2718fd-d2b0-4ed2-9074-9bcc2f7c28d2";

        given()
                .contentType(ContentType.JSON)
                .header(USER_ID_HEADER_NAME, 11)
                .when()
                .patch(BASE_URI + "/invitations/{invitationId}/accept", invitationId)
                .then()
                .log().all()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .assertThat()
                .body("message", is("Invitation time has expired"));

    }

    @Test
    @DisplayName("Accept invitation of organization when wrong user expected forbidden")
    public void acceptInvitationWhenWrongUserExpectedForbidden() {
        final String invitationId = "6d2718fd-d2b0-4ed2-9074-9bcc2f7c28d2";

        given()
                .contentType(ContentType.JSON)
                .header(USER_ID_HEADER_NAME, 10)
                .when()
                .patch(BASE_URI + "/invitations/{invitationId}/accept", invitationId)
                .then()
                .log().all()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .assertThat()
                .body("message", is("You can't accept this invitation"));
    }

    @Test
    @DisplayName("Accept invitation of organization when the invitation is declined expected conflict")
    public void acceptInvitationWhenInvitationDeclinedExpectedConflict() {
        final String invitationId = "6d2718fd-d2b0-4ed2-9074-9bcc2f7c28d3";

        given()
                .contentType(ContentType.JSON)
                .header(USER_ID_HEADER_NAME, 12)
                .when()
                .patch(BASE_URI + "/invitations/{invitationId}/accept", invitationId)
                .then()
                .log().all()
                .statusCode(HttpStatus.CONFLICT.value())
                .assertThat()
                .body("message", is("Invitation is already declined"));
    }

    @Test
    @DisplayName("Accept invitation of organization when the invitation is accepted expected conflict")
    public void acceptInvitationWhenInvitationAcceptedExpectedConflict() {
        final String invitationId = "6d2718fd-d2b0-4ed2-9074-9bcc2f7c28d4";

        given()
                .contentType(ContentType.JSON)
                .header(USER_ID_HEADER_NAME, 13)
                .when()
                .patch(BASE_URI + "/invitations/{invitationId}/accept", invitationId)
                .then()
                .log().all()
                .statusCode(HttpStatus.CONFLICT.value())
                .assertThat()
                .body("message", is("Invitation is already accepted"));
    }

    @Test
    @DisplayName("Decline invitation of organization expected success")
    public void declineInvitationOfOrganizationExpectedSuccess() {
        final String invitationId = "6d2718fd-d2b0-4ed2-9074-9bcc2f7c28d1";

        given()
                .contentType(ContentType.JSON)
                .header(USER_ID_HEADER_NAME, 10)
                .when()
                .patch(BASE_URI + "/invitations/{invitationId}/decline", invitationId)
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .assertThat()
                .body("id", notNullValue())
                .body("organization", notNullValue())
                .body("userId", is(10))
                .body("status", is(InvitationStatus.DECLINED.name()))
                .body("roleId", is(2))
                .body("invitorId", is(1))
                .body("expiredAt", notNullValue());

    }

    @Test
    @DisplayName("Get invitation expected success response")
    public void getInvitationExpectedSuccess() {
        final String invitationId = "6d2718fd-d2b0-4ed2-9074-9bcc2f7c28d1";

        given()
                .contentType(ContentType.JSON)
                .header(USER_ID_HEADER_NAME, 10)
                .when()
                .get(BASE_URI + "/invitations/{invitationId}", invitationId)
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .assertThat()
                .body("id", notNullValue())
                .body("organization", notNullValue())
                .body("userId", is(10))
                .body("status", is(InvitationStatus.PENDING.name()))
                .body("roleId", is(2))
                .body("invitorId", is(1))
                .body("expiredAt", notNullValue());

    }

}