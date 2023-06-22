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
    public void saveIfNotExist(IpoOrder ipoOrder) {
        this.ipoOrderRepository.findBySid(ipoOrder.getSid())
                .flatMap(existingUser -> {
                    System.out.println("User with ID already exists");
                    return Mono.error(new IllegalStateException("User with ID already exists"));
                })
                .switchIfEmpty(this.ipoOrderRepository.save(ipoOrder)
                        .doOnSuccess(savedOrder -> System.out.println("Order saved successfully: " + savedOrder))
                        .doOnError(error -> System.out.println("Error occurred while saving order: " + error)))
                .subscribe();
    }
}
