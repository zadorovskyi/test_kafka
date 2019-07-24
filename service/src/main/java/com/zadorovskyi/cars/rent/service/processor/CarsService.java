package com.zadorovskyi.cars.rent.service.processor;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zadorovskyi.cars.rent.service.api.NewCarRequest;
import com.zadorovskyi.cars.rent.service.kafka.KafkaProducer;
import com.zadorovskyi.cars.rent.service.kafka.api.model.NewCarEvent;
import com.zadorovskyi.cars.rent.service.service.QRCodeService;

@Component
public class CarsService {

    private final KafkaProducer kafkaProducer;

    private final QRCodeService qrCodeService;

    @Autowired
    public CarsService(KafkaProducer kafkaProducer, QRCodeService qrCodeService) {
        this.kafkaProducer = kafkaProducer;
        this.qrCodeService = qrCodeService;
    }

    public byte[] addNewCar(NewCarRequest newCarRequest) {
        String processingId = UUID.randomUUID().toString();
        NewCarEvent newCarKafkaEvent = mapToKafkaEvent(processingId, newCarRequest);
        kafkaProducer.push(newCarKafkaEvent);
        return qrCodeService.getQRCodeImageBytes(newCarKafkaEvent, 100, 100);
    }

    private NewCarEvent mapToKafkaEvent(String processingId, NewCarRequest newCarRequest) {
        return NewCarEvent.builder()
            .processingId(processingId)
            .brandName(newCarRequest.getBrandName())
            .carUUID(newCarRequest.getCarUUID())
            .color(newCarRequest.getColor())
            .fuelType(newCarRequest.getFuelType().name())
            .millage(newCarRequest.getMillage())
            .build();
    }
}
