package com.zadorovskyi.zars.rent.service.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zadorovskyi.cars.rent.service.api.NewCarRequest;
import com.zadorovskyi.cars.rent.service.processor.CarsService;

@RestController
@RequestMapping("/rent/service")
public class CarsController {

    private final CarsService carsService;

    @Autowired
    public CarsController(CarsService carsService) {
        this.carsService = carsService;
    }

    @PostMapping("/cars")
    public byte[] addCar(@RequestBody NewCarRequest newCarRequest) {
        return carsService.addNewCar(newCarRequest);
    }
    @GetMapping("/cars")
    public void testGet() {
        System.out.println("GET");
    }

}
