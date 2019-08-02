package com.zadorovskyi.cars.rent.service.web.config;

import javax.validation.constraints.NotNull;

import com.zadorovskyi.cars.rent.service.kafka.config.KafkaClientConfiguration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationConfiguration {

    @NotNull
    private KafkaClientConfiguration kafkaClient;
}
