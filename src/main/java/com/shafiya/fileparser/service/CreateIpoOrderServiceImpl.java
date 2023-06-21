package com.shafiya.fileparser.service;

import com.shafiya.fileparser.entity.IpoOrder;
import com.shafiya.fileparser.repository.IpoOrderRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CreateIpoOrderServiceImpl implements CreateIpoOrderService {
    private final IpoOrderRepository ipoOrderRepository;

    public CreateIpoOrderServiceImpl(IpoOrderRepository ipoOrderRepository) {
        this.ipoOrderRepository = ipoOrderRepository;
    }

    @Override
    public Mono<Void> saveIfNotExist(IpoOrder ipoOrder) {
        return ipoOrderRepository.findBySid(ipoOrder.getSid()).
                flatMap(existingUser -> Mono.error(new IllegalStateException("User with ID already exists"))).
                switchIfEmpty(this.ipoOrderRepository.save(ipoOrder))
                .then();
    }
}
