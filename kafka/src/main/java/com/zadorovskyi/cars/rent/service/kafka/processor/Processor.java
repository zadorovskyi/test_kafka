package com.zadorovskyi.cars.rent.service.kafka.processor;

import java.util.concurrent.TimeUnit;

import org.apache.kafka.streams.processor.AbstractProcessor;
import org.springframework.stereotype.Component;

import com.zadorovskyi.cars.rent.service.kafka.api.model.NewCarEvent;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Component
public class Processor extends AbstractProcessor<String, NewCarEvent> {

    @Override public void process(String key, NewCarEvent value) {
        value.getBrandName();
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("Kafka event have been received successfully Event={}", value);
    }
}