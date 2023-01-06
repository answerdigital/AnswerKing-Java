package com.answerdigital.answerking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class AnswerKingApplication {
    public static void main(final String[] args) {
	SpringApplication.run(AnswerKingApplication.class, args);
    }
}
