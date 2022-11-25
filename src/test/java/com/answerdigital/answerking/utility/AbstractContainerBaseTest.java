package com.answerdigital.answerking.utility;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;

@ActiveProfiles("test")
public abstract class AbstractContainerBaseTest {
    private static final MySQLContainer MYSQL_CONTAINER;

    static {
        MYSQL_CONTAINER =
                new MySQLContainer<>("mysql:8.0.31")
                        .withInitScript("init_db.sql")
                        .withReuse(true);
        MYSQL_CONTAINER.start();
    }

    @DynamicPropertySource
    private static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MYSQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", MYSQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", MYSQL_CONTAINER::getPassword);
        registry.add("spring.datasource.name", MYSQL_CONTAINER::getDatabaseName);
    }
}