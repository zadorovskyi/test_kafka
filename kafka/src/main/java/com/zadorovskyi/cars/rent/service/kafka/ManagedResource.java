package com.zadorovskyi.cars.rent.service.kafka;

import javax.annotation.PreDestroy;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;

public interface ManagedResource {

    @EventListener(ApplicationStartedEvent.class)
    void start();

    @PreDestroy
    void stop();

}
