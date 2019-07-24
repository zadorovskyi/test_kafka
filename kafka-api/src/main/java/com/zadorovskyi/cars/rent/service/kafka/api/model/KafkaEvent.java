package com.zadorovskyi.cars.rent.service.kafka.api.model;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KafkaEvent {

    private Instant endDate;

    private Instant startDate;

    private EventDescription eventDescription;

}
