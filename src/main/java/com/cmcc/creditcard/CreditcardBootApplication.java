package com.cmcc.creditcard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CreditcardBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(CreditcardBootApplication.class, args);
    }

}
