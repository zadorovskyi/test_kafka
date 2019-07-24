package com.zadorovskyi.cars.rent.service.api;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewCarRequest {

    private UUID carUUID;

    private String brandName;

    private String color;

    private Long millage;

    private FuelType fuelType;

}
