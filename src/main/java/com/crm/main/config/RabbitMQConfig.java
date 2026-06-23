package com.crm.main.config;

import com.crm.sharedlib.messaging.config.BaseRabbitMQConfig;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.crm.sharedlib.messaging.constants.RabbitMQConstants.*;

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
    public Queue sendInvitationOfOrganizationQueue() {
        return QueueBuilder
                .durable(SEND_INVITATION_OF_ORGANIZATION_ROUTING_KEY)
                .build();
    }

    @Bean
    public Queue createRootDirQueue() {
        return QueueBuilder
                .durable(CREATE_ROOT_DIR_QUEUE)
                .build();
    }

    @Bean
    public Queue createDefaultTaskPrioritiesQueue() {
        return QueueBuilder
                .durable(CREATE_DEFAULT_TASK_PRIORITIES_QUEUE)
                .build();
    }

    @Bean
    public Queue createDefaultTaskStatusesQueue() {
        return QueueBuilder
                .durable(CREATE_DEFAULT_TASK_STATUSES_QUEUE)
                .build();
    }

    @Bean
    public Queue orgRootDirCreatedQueue() {
        return QueueBuilder
                .durable(ROOT_DIR_CREATED_REPLY_QUEUE)
                .build();
    }

    @Bean
    public Queue taskReminderQueue() {
        return QueueBuilder
                .durable(REMIND_ABOUT_TASK_QUEUE)
                .build();
    }

    @Bean
    public Binding remindAboutTaskBinding() {
        return BindingBuilder.bind(taskReminderQueue())
                .to(mainServiceTopicExchanger())
                .with(TASK_REMINDER_ROUTING_KEY);
    }

    @Bean
    public Binding createOrgRootDirBinding() {
        return BindingBuilder
                .bind(createRootDirQueue())
                .to(mainServiceTopicExchanger())
                .with(ORGANIZATION_CREATED_ROUTING_KEY);
    }

    @Bean
    public Binding createDefaultTaskPrioritiesBinding() {
        return BindingBuilder
                .bind(createDefaultTaskPrioritiesQueue())
                .to(mainServiceTopicExchanger())
                .with(ORGANIZATION_CREATED_ROUTING_KEY);
    }

    @Bean
    public Binding createDefaultTaskStatusesBinding() {
        return BindingBuilder
                .bind(createDefaultTaskStatusesQueue())
                .to(mainServiceTopicExchanger())
                .with(ORGANIZATION_CREATED_ROUTING_KEY);
    }

    @Bean
    public Binding OrgUserRoleChangeBinding() {
        return BindingBuilder
                .bind(orgUserSyncRoles())
                .to(mainServiceTopicExchanger())
                .with(ORGANIZATION_USER_ROLE_CHANGE_ROUTING_KEY);
    }

    @Bean
    public Binding invitationCreatedBinding() {
        return BindingBuilder
                .bind(sendInvitationOfOrganizationQueue())
                .to(mainServiceTopicExchanger())
                .with(ORGANIZATION_INVITATION_CREATED_ROUTING_KEY);
    }


}
