package com.zadorovskyi.cars.rent.service.event;

import org.springframework.context.ApplicationEvent;

import com.zadorovskyi.cars.rent.service.api.RestartableResource;

import lombok.Data;

@Data
public class RestartableEvent<T extends RestartableResource> extends ApplicationEvent {

    private T resource;

    private String description;

    public RestartableEvent(Object source, T resource, String description) {
        super(source);
        this.resource = resource;
        this.description = description;
    }
}