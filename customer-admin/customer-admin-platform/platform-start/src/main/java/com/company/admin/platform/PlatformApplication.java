package com.company.admin.platform;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.company.admin")
@MapperScan("com.company.admin.platform.*.infrastructure.dao")
public class PlatformApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(PlatformApplication.class, args);
    }
}
