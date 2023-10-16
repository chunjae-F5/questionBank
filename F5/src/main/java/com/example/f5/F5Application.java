package com.example.f5;

import com.example.f5.util.BaseTimeEntity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class F5Application {

    public static void main(String[] args) {
        SpringApplication.run(F5Application.class, args);
    }

}
