package com.crm.main.service.consumer;

import com.crm.main.persistance.entity.Organization;
import com.crm.main.service.OrganizationFileService;
import com.crm.main.service.OrganizationService;
import com.crm.sharedlib.messaging.dto.amqp.OrgRootDirCreatedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.crm.sharedlib.messaging.constants.RabbitMQConstants.ROOT_DIR_CREATED_REPLY_QUEUE;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrganizationRootDirCreatedConsumer {

    private final OrganizationService organizationService;
    private final OrganizationFileService fileService;

    @RabbitListener(queues = ROOT_DIR_CREATED_REPLY_QUEUE)
    public void organizationRootDirCreated(OrgRootDirCreatedMessage message) {
        UUID fileId = message.getFileId();
        Long organizationId = message.getOrganizationId();

        log.debug("Root dir {} has been received for organization {}", fileId, organizationId);

        try {
            Organization organization =
                    organizationService.getOrganizationOrThrowException(organizationId);

            fileService.createRootOrganizationFile(organization, fileId);

            log.debug("Organization {} root dir {} has been created", organizationId, fileId);
        } catch (Exception e) {
            log.debug("Error has occurred while saving root dir {} for an organization {}", fileId, organizationId, e);
            throw e;
        }
    }

}
