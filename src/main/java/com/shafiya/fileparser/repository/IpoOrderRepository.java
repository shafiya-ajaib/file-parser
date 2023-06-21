package com.shafiya.fileparser.repository;

import com.shafiya.fileparser.entity.IpoOrder;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface IpoOrderRepository extends ReactiveCrudRepository<IpoOrder, Long> {
    Mono<IpoOrder> findBySid(String sid);
}
