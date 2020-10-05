package com.hb.isblog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.hb.isblog.dao")
@SpringBootApplication
public class IsblogApplication {

    public static void main(String[] args) {
        SpringApplication.run(IsblogApplication.class, args);
    }

}
