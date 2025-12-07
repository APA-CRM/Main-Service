package com.crm.main.config;

import com.crm.sharedlib.messaging.config.BaseRabbitMQConfig;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.crm.main.constants.RabbitConstants.*;
import static com.crm.sharedlib.messaging.constants.RabbitMQConstants.ORGANIZATION_USER_ROLES_SYNC_QUEUE;
import static com.crm.sharedlib.messaging.constants.RabbitMQConstants.SEND_INVITATION_OF_ORGANIZATION;

@Configuration
@EnableRabbit
public class RabbitMQConfig extends BaseRabbitMQConfig {

    @Value("${app.ampq.organisation-user-sync-roles.queue.ttl}")
    private Integer orgUserSyncRolesQueueTtl;

    @Bean
    public TopicExchange mainServiceTopicExchanger() {
        return ExchangeBuilder
                .topicExchange(MAIN_SERVICE_EXCHANGER_NAME)
                .durable(true)
                .build();
    }

    @Bean
    public Queue orgUserSyncRoles() {
        return QueueBuilder
                .durable(ORGANIZATION_USER_ROLES_SYNC_QUEUE)
                .ttl(orgUserSyncRolesQueueTtl)
                .build();
    }

    @Bean
    public Binding OrgUserRoleChangeBinding(
            TopicExchange mainServiceTopicExchanger,
            Queue orgUserSyncRoles
    ) {
        return BindingBuilder
                .bind(orgUserSyncRoles)
                .to(mainServiceTopicExchanger)
                .with(ORGANIZATION_USER_ROLE_CHANGE_ROUTING_KEY);
    }

    @Bean
    public Queue sendInvitationOfOrganizationQueue() {
        return QueueBuilder
                .durable(SEND_INVITATION_OF_ORGANIZATION)
                .build();
    }

    @Bean
    public Binding invitationCreatedBinding(
            TopicExchange mainServiceTopicExchanger,
            Queue sendInvitationOfOrganizationQueue
    ) {
        return BindingBuilder
                .bind(sendInvitationOfOrganizationQueue)
                .to(mainServiceTopicExchanger)
                .with(ORGANIZATION_INVITATION_CREATED);
    }


}
