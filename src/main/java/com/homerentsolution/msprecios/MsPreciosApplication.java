package com.homerentsolution.msprecios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsPreciosApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsPreciosApplication.class, args);
    }

}
