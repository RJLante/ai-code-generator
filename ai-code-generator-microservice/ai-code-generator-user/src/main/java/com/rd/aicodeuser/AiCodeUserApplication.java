package com.rd.aicodeuser;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDubbo
@MapperScan("com.rd.aicodeuser.mapper")
@ComponentScan("com.rd")
public class AiCodeUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(AiCodeUserApplication.class, args);
    }
}