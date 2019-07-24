package com.zadorovskyi.cars.rent.service.kafka.api.model;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewCarEvent {

    private String processingId;

    private UUID carUUID;

    private String brandName;

    private String color;

    private Long millage;

    private String fuelType;

}
