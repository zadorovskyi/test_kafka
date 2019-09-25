package com.zadorovskyi.cars.rent.service.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zadorovskyi.cars.rent.service.api.model.NewCarRequest;
import com.zadorovskyi.cars.rent.service.processor.CarsService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/rent/service")
public class CarsController {

    private final CarsService carsService;

    @Autowired
    public CarsController(CarsService carsService) {
        this.carsService = carsService;
    }

    @PostMapping(value = "/cars", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] addCar(@RequestBody NewCarRequest newCarRequest) {
        log.info("Request received. Request={}", newCarRequest);
        return carsService.addNewCar(newCarRequest);
    }

    @GetMapping("/cars")
    public void testGet() {
        System.out.println("GET");
    }

}
