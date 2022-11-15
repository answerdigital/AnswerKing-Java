package com.answerdigital.answerking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AnswerKingApplication {
    public static void main(final String[] args) {
        SpringApplication.run(AnswerKingApplication.class, args);
        while (true) {
            System.out.println("fail me");
        }
    }
}
