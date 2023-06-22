package com.shafiya.fileparser.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface CreateIpoOrderListener {
    void onReceive(String json, Integer deliveryAttempt, String topic) throws JsonProcessingException;
}
