package com.crm.main;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.RabbitMQContainer;

public abstract class BaseIntegrationTestWithRabbitMQ extends BaseIntegrationTest {

    private static RabbitMQContainer rabbitMQContainer;

    @BeforeAll
    public static void configureRabbit() {
        rabbitMQContainer = new RabbitMQContainer("rabbitmq:4.1-management");
        rabbitMQContainer.withExposedPorts(5672, 15672);

        rabbitMQContainer.start();

        System.setProperty("spring.rabbitmq.host", rabbitMQContainer.getHost());
        System.setProperty("spring.rabbitmq.port", rabbitMQContainer.getAmqpPort().toString());
    }

    @AfterAll
    public static void shutdownRabbit() {
        rabbitMQContainer.close();
    }


}
