package com.zadorovskyi.cars.rent.service.kafka;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.Topology;
import org.springframework.beans.factory.annotation.Autowired;

import com.zadorovskyi.cars.rent.service.api.RestartableResource;
import com.zadorovskyi.cars.rent.service.kafka.config.KafkaProperties;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public abstract class RestartableKStream implements RestartableResource {

    @Autowired
    private KafkaProperties kafkaProperties;

    private KafkaStreams kafkaStreams;

    @Autowired
    private UncaughtThrowableHolder exceptionHolder;

    @Override
    public void start() {
        Topology topology = this.buildTopology();
        this.kafkaStreams = new KafkaStreams(topology, this.kafkaProperties.createProperties());

        this.kafkaStreams.setUncaughtExceptionHandler((tread, ex) -> {
                this.exceptionHolder.uncaughtException(tread, ex);
                this.uncaughtException(tread, ex);
            }
        );
        this.onStart();
    }

    @Override
    public void stop() {
        this.kafkaStreams.close();
    }

    @Override
    public void restart() {
        try {
            this.stop();
            this.start();
            log.info("Stream was successfully restarted");
        } catch (Exception ex) {
            log.error("Couldn't restart Kafka stream");
            throw new RuntimeException(ex);
        }
    }

    private void onStart() {
        this.kafkaStreams.start();
    }

    public abstract Topology buildTopology();

    public abstract void uncaughtException(Thread t, Throwable e);
}
