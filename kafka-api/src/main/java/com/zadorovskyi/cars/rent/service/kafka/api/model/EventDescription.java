package com.zadorovskyi.cars.rent.service.kafka.api.model;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDescription {

    private String eventName;

    private Integer eventDuration;

    private UUID eventId;

}
