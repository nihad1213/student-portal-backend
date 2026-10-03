package com.spb.studentportalbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@EntityScan(basePackages = "com.spb.studentportalbackend.entity" )
public class StudentPortalBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentPortalBackendApplication.class, args);
    }

}
