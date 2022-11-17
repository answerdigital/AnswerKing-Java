package com.answerdigital.answerking.utility;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;

@ActiveProfiles("test")
public abstract class ContainerisedTest {
    private static final MySQLContainer container =
            new MySQLContainer<>("mysql:8.0.31")
                    .withDatabaseName("answer_king_test")
                    .withUsername("test_user")
                    .withPassword("GS3ef_fsd^!")
                    .withReuse(true);

    @BeforeAll
    public static void startup() {
        container.start();
    }

    @AfterAll
    public static void shutdown() {
        container.stop();
    }

    @DynamicPropertySource
    private static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
    }
}
