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
                .switchIfEmpty(this.ipoOrderRepository.save(ipoOrder)
                        .onErrorMap(throwable -> new RuntimeException("Error saving order", throwable)))
                .onErrorMap(throwable -> new RuntimeException("Error finding order", throwable))
                .block();;
    }
}
