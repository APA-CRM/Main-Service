package com.crm.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import jakarta.annotation.PostConstruct;
import org.jobrunr.storage.StorageProvider;
import org.jobrunr.storage.sql.h2.H2StorageProvider;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@ActiveProfiles("test")
@Import(BaseIntegrationTest.Configuration.class)
public abstract class BaseIntegrationTest {

    protected final ObjectMapper objectMapper = new ObjectMapper();

    @LocalServerPort
    private int localServerPort;

    @PostConstruct
    public void init() {
        RestAssured.port = localServerPort;
        objectMapper.findAndRegisterModules();
    }

    @TestConfiguration
    public static class Configuration {

        @Bean
        @Primary
        public StorageProvider testStorageProvider(DataSource dataSource) {
            return new H2StorageProvider(dataSource);
        }

    }

}
