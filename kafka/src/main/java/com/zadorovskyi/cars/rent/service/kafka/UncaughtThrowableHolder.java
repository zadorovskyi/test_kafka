package com.zadorovskyi.cars.rent.service.kafka;

import java.util.Optional;

import org.springframework.stereotype.Component;

@Component
public class UncaughtThrowableHolder implements Thread.UncaughtExceptionHandler {

    private Thread thread;

    private Throwable throwable;

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        this.thread = t;
        this.throwable = e;
    }

    public Optional<Throwable> getThrowable() {
        return Optional.ofNullable(throwable);
    }

    public void clearHealthCheck() {
        this.throwable = null;
        this.thread = null;
    }
}
