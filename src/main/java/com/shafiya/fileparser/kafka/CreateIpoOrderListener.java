package com.shafiya.fileparser.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import reactor.core.publisher.Mono;

public interface CreateIpoOrderListener {
    Mono<Void> onReceive(String json, Integer deliveryAttempt, String topic) throws JsonProcessingException;
}
