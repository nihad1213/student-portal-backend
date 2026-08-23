package com.spb.studentportalbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class StudentPortalBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentPortalBackendApplication.class, args);
    }

}
