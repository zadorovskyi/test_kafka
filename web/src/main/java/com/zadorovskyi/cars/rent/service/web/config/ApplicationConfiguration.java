package com.zadorovskyi.cars.rent.service.web.config;

import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Component;

import com.zadorovskyi.cars.rent.service.kafka.config.KafkaProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationConfiguration {

    @NotNull
    private KafkaProperties kafkaClient;

    @NotNull
    private QrCodeProperties qrCodeProperties;

    private SystemProperties systemProperties;
}
