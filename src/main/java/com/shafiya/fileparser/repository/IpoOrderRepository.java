package com.shafiya.fileparser.repository;

import com.shafiya.fileparser.entity.IpoOrder;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface IpoOrderRepository extends ReactiveCrudRepository<IpoOrder, Long> {
}
