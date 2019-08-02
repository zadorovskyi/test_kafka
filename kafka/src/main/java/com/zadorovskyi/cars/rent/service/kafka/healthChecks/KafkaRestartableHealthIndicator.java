package com.zadorovskyi.cars.rent.service.kafka.healthChecks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

import com.zadorovskyi.cars.rent.service.api.healthChecks.RestartableHealth;
import com.zadorovskyi.cars.rent.service.api.healthChecks.RestartableHealthIndicator;
import com.zadorovskyi.cars.rent.service.kafka.RestartableKStream;
import com.zadorovskyi.cars.rent.service.kafka.config.KafkaClientConfiguration;

@Component
public class KafkaRestartableHealthIndicator extends KafkaConsumerHealthIndicator implements RestartableHealthIndicator {

    @Autowired
    public KafkaRestartableHealthIndicator(RestartableKStream kStream,
        KafkaClientConfiguration config) {
        super(kStream, config);
    }

    public RestartableHealth restartIfUnhealthy() {
        Health health = health();
        return RestartableHealth.<RestartableKStream>builder()
            .health(health)
            .resource(this.getKStream())
            .build();
    }
}
