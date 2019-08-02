package com.zadorovskyi.cars.rent.service.kafka;

import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.zadorovskyi.cars.rent.service.kafka.api.model.NewCarEvent;
import com.zadorovskyi.cars.rent.service.kafka.processor.Processor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RestartableKStreamConsumer extends RestartableKStream {

    @Qualifier("kafka.topic")
    private String kafkaTopic;

    @Autowired
    public RestartableKStreamConsumer(String kafkaTopic) {
        this.kafkaTopic = kafkaTopic;
    }

    @Override
    public Topology buildTopology() {
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        streamsBuilder.<String, NewCarEvent>stream(kafkaTopic).process(Processor::new);
        return streamsBuilder.build();
    }

    @Override public void uncaughtException(Thread t, Throwable e) {
        log.error("Error occurred in thread={}. Exception={}", t.getName(), e);
    }
}

