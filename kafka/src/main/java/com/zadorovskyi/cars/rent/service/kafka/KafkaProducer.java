package com.zadorovskyi.cars.rent.service.kafka;

import java.util.UUID;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.zadorovskyi.cars.rent.service.api.ManagedResource;
import com.zadorovskyi.cars.rent.service.kafka.api.model.NewCarEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class KafkaProducer implements ManagedResource {

    @Qualifier("kafka.topic")
    private String kafkaTopic;

    private Producer producer;

    @Autowired
    public KafkaProducer(String kafkaTopic, Producer producer) {
        this.kafkaTopic = kafkaTopic;
        this.producer = producer;
    }

    public void push(NewCarEvent event) {
        ProducerRecord<String, NewCarEvent> record = new ProducerRecord<>(kafkaTopic, UUID.randomUUID().toString(), event);

        producer.send(record, (recordMetadata, e) -> {
            if (e == null) {
                log.info("Kafka event={} have been successfully sent to topic={}", event, recordMetadata.topic());
            } else {
                log.error("Kafka event sent failed. Exception={}", e.getMessage());
                throw new RuntimeException("Kafka producer is down", e);
            }
        });
        producer.flush();
    }

    @Override public void start() {
        //Do nothing
    }

    @Override public void stop() {
        producer.close();
    }
}
