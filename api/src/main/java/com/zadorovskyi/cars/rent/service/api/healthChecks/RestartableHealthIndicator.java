package com.zadorovskyi.cars.rent.service.api.healthChecks;

public interface RestartableHealthIndicator {

    RestartableHealth restartIfUnhealthy();

}
