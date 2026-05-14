package com.example.electrical;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.electrical.mapper")
public class ElectricalManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(ElectricalManagementApplication.class, args);
    }
}
