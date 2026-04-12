package com.crm.main.service.consumer;

import com.crm.main.BaseIntegrationTestWithRabbitMQ;
import com.crm.main.persistance.entity.Organization;
import com.crm.main.persistance.entity.OrganizationFile;
import com.crm.main.persistance.repository.OrganizationFileRepository;
import com.crm.main.persistance.repository.OrganizationRepository;
import com.crm.sharedlib.messaging.dto.amqp.OrgRootDirCreatedMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.jdbc.Sql;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

import static com.crm.sharedlib.messaging.constants.RabbitMQConstants.ROOT_DIR_CREATED_REPLY_QUEUE;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Sql(scripts = "classpath:sql/insertTestOrganizations.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {
        "classpath:sql/deleteTestOrganizationFile.sql",
        "classpath:sql/deleteTestOrganization.sql"
}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class OrganizationRootDirCreatedConsumerTest extends BaseIntegrationTestWithRabbitMQ {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private OrganizationRepository organizationRepository;

    @MockitoSpyBean
    private OrganizationFileRepository fileRepository;

    @Test
    @DisplayName("Organization root dir created expected success")
    public void organizationRootDirCreatedExpectedSuccess() {

        OrgRootDirCreatedMessage message = new OrgRootDirCreatedMessage(100L, UUID.randomUUID());

        rabbitTemplate.convertAndSend(ROOT_DIR_CREATED_REPLY_QUEUE, message);

        await()
                .atMost(Duration.ofSeconds(5))
                .untilAsserted(() ->
                        Mockito.verify(fileRepository, Mockito.atLeastOnce())
                                .save(Mockito.any())
                );

        Optional<Organization> organizationOptional = organizationRepository.findById(message.getOrganizationId());

        assertTrue(organizationOptional.isPresent());

        Optional<OrganizationFile> fileOptional =
                fileRepository.findByOrganizationAndIsRootTrue(organizationOptional.get());

        assertTrue(fileOptional.isPresent());
    }

}