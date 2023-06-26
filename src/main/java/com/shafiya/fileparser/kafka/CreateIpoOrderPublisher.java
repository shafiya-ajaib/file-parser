package com.shafiya.fileparser.kafka;

import com.shafiya.fileparser.entity.IpoOrder;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicInteger;

public interface CreateIpoOrderPublisher {
    Mono<Boolean> publish(IpoOrder message);
}
