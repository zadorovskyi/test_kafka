package com.zadorovskyi.cars.rent.service.api.healthChecks;

import org.springframework.boot.actuate.health.Health;

import com.zadorovskyi.cars.rent.service.api.RestartableResource;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RestartableHealth<T extends RestartableResource> {

    private T resource;

    private Health health;
}
