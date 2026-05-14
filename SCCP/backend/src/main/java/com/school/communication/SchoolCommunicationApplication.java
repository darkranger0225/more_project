package com.school.communication;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.school.communication.mapper")
public class SchoolCommunicationApplication {
    public static void main(String[] args) {
        SpringApplication.run(SchoolCommunicationApplication.class, args);
    }
}