package com.zadorovskyi.cars.rent.service.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = "com.zadorovskyi.cars.rent.service")
@EnableScheduling
public class CarsRentApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarsRentApplication.class, args);
    }

}
