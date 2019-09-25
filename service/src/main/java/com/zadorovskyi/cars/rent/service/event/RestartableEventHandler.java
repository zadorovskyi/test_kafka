package com.zadorovskyi.cars.rent.service.event;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.zadorovskyi.cars.rent.service.api.RestartableResource;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RestartableEventHandler {

    private Map<FailedCheck, Integer> failedHealthChecks;

    public RestartableEventHandler() {
        this.failedHealthChecks = new HashMap<>();
    }

    @EventListener
    public void onApplicationEvent(RestartableEvent event) {
        FailedCheck fail = FailedCheck.builder()
            .resource(event.getResource())
            .description(event.getDescription())
            .build();
        if (needRestart(fail)) {
            restartResource(event.getResource());
        }
        incrementFails(fail);
    }

    private void restartResource(RestartableResource resource) {
        log.info("Trying to restart service={}", resource.getClass().getName());
        resource.restart();
        clearMap();
    }

    private boolean needRestart(FailedCheck key) {
        return Optional.ofNullable(failedHealthChecks.get(key))
            .filter(count -> count >= 2)
            .isPresent();

    }

    private void incrementFails(FailedCheck event) {
        if (failedHealthChecks.containsKey(event)) {
            failedHealthChecks.put(event, failedHealthChecks.get(event) + 1);
        } else {
            failedHealthChecks.put(event, 0);
        }
    }

    @Scheduled(fixedRate = 13000)
    private void clearMap() {
        failedHealthChecks.clear();
    }

    @Data
    @Builder
    static class FailedCheck {

        private Object resource;

        private String description;

    }
}