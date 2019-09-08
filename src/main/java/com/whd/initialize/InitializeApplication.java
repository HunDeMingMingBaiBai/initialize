package com.whd.initialize;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.whd.initialize.mapper") // Mybatis无配置文件
public class InitializeApplication {

    public static void main(String[] args) {
        SpringApplication.run(InitializeApplication.class, args);
    }

}
