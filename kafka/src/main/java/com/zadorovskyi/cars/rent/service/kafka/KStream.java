package com.zadorovskyi.cars.rent.service.kafka;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.Topology;
import org.springframework.beans.factory.annotation.Autowired;

import com.zadorovskyi.cars.rent.service.kafka.config.KafkaClientConfiguration;

import lombok.Data;

@Data
public abstract class KStream implements ManagedResource {

    @Autowired
    private KafkaClientConfiguration kafkaClientConfiguration;

    private KafkaStreams kafkaStreams;

    @Override
    public void start() {
        Topology topology = this.buildTopology();
        this.kafkaStreams = new KafkaStreams(topology, this.kafkaClientConfiguration.createProperties());
        this.kafkaStreams.setUncaughtExceptionHandler(this::uncaughtException);
        this.onStart();
    }

    @Override
    public void stop() {
        this.kafkaStreams.close();
    }

    private void onStart() {
        this.kafkaStreams.start();
    }

    public abstract Topology buildTopology();

    public abstract void uncaughtException(Thread t, Throwable e);
}
