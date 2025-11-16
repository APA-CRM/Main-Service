package com.crm.main.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.crm.main.constants.RabbitConstants.*;
import static com.crm.sharedlib.consts.CrmConstants.*;

@Configuration
@EnableRabbit
public class RabbitMQConfig {

    @Value("${spring.rabbitmq.host}")
    private String host;
    @Value("${spring.rabbitmq.port}")
    private Integer port;
    @Value("${spring.rabbitmq.username}")
    private String username;
    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value("${app.ampq.organisation-user-sync-roles.queue.ttl}")
    private Integer orgUserSyncRolesQueueTtl;

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory(host);

        factory.setHost(host);
        factory.setPort(port);
        factory.setUsername(username);
        factory.setPassword(password);

        return factory;
    }

    @Bean
    public AmqpTemplate amqpTemplate(
            MessageConverter messageConverter,
            ConnectionFactory connectionFactory
    ) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setMessageConverter(messageConverter);
        rabbitTemplate.setConnectionFactory(connectionFactory);

        return rabbitTemplate;
    }

    @Bean
    public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }


    @Configuration
    public class ProducerConfig {

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

}
