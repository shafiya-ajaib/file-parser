package com.shafiya.fileparser.kafka;

import com.shafiya.fileparser.entity.IpoOrder;
import reactor.core.publisher.Mono;

public interface CreateIpoOrderPublisher {
    Mono<Boolean> publish(IpoOrder message);
}
