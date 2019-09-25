package com.zadorovskyi.cars.rent.service.kafka.healthChecks;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

import com.zadorovskyi.cars.rent.service.kafka.RestartableKStream;
import com.zadorovskyi.cars.rent.service.kafka.config.KafkaProperties;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import static org.apache.kafka.streams.KafkaStreams.State.CREATED;
import static org.apache.kafka.streams.KafkaStreams.State.ERROR;
import static org.apache.kafka.streams.KafkaStreams.State.NOT_RUNNING;
import static org.apache.kafka.streams.KafkaStreams.State.REBALANCING;

import static com.zadorovskyi.cars.rent.service.api.healthChecks.HealthCode.HEALTHY;
import static com.zadorovskyi.cars.rent.service.api.healthChecks.HealthCode.UNHEALTHY;

@Slf4j
@Data
@Component
public class KafkaConsumerHealthIndicator implements HealthIndicator {

    private final static Long TIMEOUT = TimeUnit.SECONDS.toMillis(3);

    private final String host;

    private final int port;

    private final RestartableKStream kStream;

    @Autowired
    public KafkaConsumerHealthIndicator(RestartableKStream kStream, KafkaProperties config) {
        this.kStream = kStream;
        this.host = config.getKafkaBrokerUrl().split(":")[0];
        this.port = Integer.valueOf(config.getKafkaBrokerUrl().split(":")[1]);
    }

    @Override
    public Health health() {
        Socket socket = null;
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(host, port), TIMEOUT.intValue());
            return checkUncaught();
        } catch (final Exception e) {
            log.warn("Kafka stream={} is not running. Exception={}", kStream, e);
            return Health.status(new Status(NOT_RUNNING.name(), e.getMessage())).build();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (final IOException e) {
                    log.warn("Unable to close Kafka consumer socket. Exception={}", e);
                }
            }
        }
    }

    private Health checkUncaught() {
        Optional<Throwable> throwable = this.kStream.getExceptionHolder().getThrowable();
        if (throwable.isPresent()) {
            return Health.status(new Status(UNHEALTHY.name(), throwable.get().getMessage()))
                .build();
        } else {
            switch (this.kStream.getKafkaStreams().state()) {
            case CREATED:
                return Health.status(new Status(CREATED.name(), "is not started"))
                    .build();
            case NOT_RUNNING:
                return Health.status(new Status(NOT_RUNNING.name(), "not running"))
                    .build();
            case ERROR:
                return Health.status(new Status(ERROR.name(), "error"))
                    .build();
            case REBALANCING:
                return Health.status(new Status(REBALANCING.name(), "is rebalancing"))
                    .build();
            default:
                log.info("Kafka stream is healthy");
                return Health.status(new Status(HEALTHY.name())).build();
            }
        }
    }
}
