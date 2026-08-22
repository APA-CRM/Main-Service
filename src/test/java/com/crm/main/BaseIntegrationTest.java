package com.crm.main;

import io.restassured.RestAssured;
import jakarta.annotation.PostConstruct;
import org.jobrunr.jobs.mappers.JobMapper;
import org.jobrunr.storage.StorageProvider;
import org.jobrunr.storage.sql.h2.H2StorageProvider;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import tools.jackson.databind.json.JsonMapper;

import javax.sql.DataSource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@ActiveProfiles("test")
@Import(BaseIntegrationTest.Configuration.class)
public abstract class BaseIntegrationTest {

    protected final JsonMapper jsonMapper = JsonMapper.builder()
            .findAndAddModules()
            .build();

    @LocalServerPort
    private int localServerPort;

    @PostConstruct
    public void init() {
        RestAssured.port = localServerPort;
    }

    @ContextConfiguration
    public static class Configuration {

        @Bean
        @Primary
        public StorageProvider testStorageProvider(DataSource dataSource, JobMapper jobMapper) {
            H2StorageProvider storageProvider = new H2StorageProvider(dataSource);
            storageProvider.setJobMapper(jobMapper);

            return storageProvider;
        }
    }

}
