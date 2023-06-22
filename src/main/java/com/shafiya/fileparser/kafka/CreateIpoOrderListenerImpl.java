package com.shafiya.fileparser.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shafiya.fileparser.entity.IpoOrder;
import com.shafiya.fileparser.service.CreateIpoOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

@Slf4j
@Component
public class CreateIpoOrderListenerImpl implements CreateIpoOrderListener {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final CreateIpoOrderService createIpoOrderService;

    public CreateIpoOrderListenerImpl(CreateIpoOrderService createIpoOrderService) {
        this.createIpoOrderService = createIpoOrderService;
    }

    @Override
    @KafkaListener(topics = "com.shafiya.fileparser.CreateIpoOrder", groupId = "ipo-order123")
    public void onReceive(String json,
                          @Header(value = KafkaHeaders.DELIVERY_ATTEMPT, required = false) Integer deliveryAttempt,
                          @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) throws JsonProcessingException {
        IpoOrder ipoOrder = objectMapper.readValue(json, IpoOrder.class);
        createIpoOrderService.saveIfNotExist(ipoOrder);
    }
}
