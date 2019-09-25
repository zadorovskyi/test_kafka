package com.zadorovskyi.cars.rent.service.healthCheck;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.zadorovskyi.cars.rent.service.api.healthChecks.RestartableHealth;
import com.zadorovskyi.cars.rent.service.api.healthChecks.RestartableHealthIndicator;
import com.zadorovskyi.cars.rent.service.event.RestartableEvent;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import static org.apache.kafka.streams.KafkaStreams.State.ERROR;
import static org.apache.kafka.streams.KafkaStreams.State.NOT_RUNNING;

import static com.zadorovskyi.cars.rent.service.api.healthChecks.HealthCode.UNHEALTHY;

@Data
@Slf4j
@Component
public class HealthChecksExecutor {

    private ApplicationEventPublisher publisher;

    public void publish(RestartableEvent event) {
        publisher.publishEvent(event);
    }

    private List<? extends RestartableHealthIndicator> healthChecks;

    @Autowired
    public HealthChecksExecutor(List<? extends RestartableHealthIndicator> healthChecks, ApplicationEventPublisher publisher) {
        this.healthChecks = healthChecks;
        this.publisher = publisher;
    }

    @Scheduled(fixedRate = 4000)
    public void check() {
        for (RestartableHealthIndicator healthCheck : healthChecks) {
            RestartableHealth health = healthCheck.checkHealth();
            String healthCode = health.getHealth().getStatus().getCode();
            if (Objects.equals(healthCode, UNHEALTHY.name()) || Objects
                .equals(healthCode, NOT_RUNNING.name()) || Objects.equals(healthCode, ERROR.name())) {

                String description = generateDescription(health.getResource().getClass(), health.getHealth());
                log.warn(description);
                publish(new RestartableEvent<>(this, health.getResource(), description));
            }
        }
    }

    private String generateDescription(Class serviceClass, Health health) {
        return String.format("Health check for service= %s failed with status= %s. Health check description=%s",
            serviceClass.getName(), health.getStatus().getCode(), health.getDetails());
    }
}
