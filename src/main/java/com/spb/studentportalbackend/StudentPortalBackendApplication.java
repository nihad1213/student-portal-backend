package com.spb.studentportalbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EntityScan(basePackages = "com.spb.studentportalbackend.entity" )
public class StudentPortalBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentPortalBackendApplication.class, args);
    }

}
