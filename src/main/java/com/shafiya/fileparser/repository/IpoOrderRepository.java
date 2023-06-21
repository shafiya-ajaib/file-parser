package com.shafiya.fileparser.repository;

import com.shafiya.fileparser.entity.IpoOrder;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface IpoOrderRepository extends ReactiveCrudRepository<IpoOrder, Long> {
    @Query("SELECT * FROM ipo_order WHERE sid = :sid")
    Mono<IpoOrder> findBySid(String sid);
}
