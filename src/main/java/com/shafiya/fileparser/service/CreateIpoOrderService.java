package com.shafiya.fileparser.service;

import com.shafiya.fileparser.entity.IpoOrder;
import reactor.core.publisher.Mono;

public interface CreateIpoOrderService {
    Mono<Void> saveIfNotExist(IpoOrder ipoOrder);
}
