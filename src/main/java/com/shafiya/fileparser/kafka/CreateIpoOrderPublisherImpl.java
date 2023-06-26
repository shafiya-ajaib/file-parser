package com.shafiya.fileparser.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shafiya.fileparser.constant.MessageConstant;
import com.shafiya.fileparser.entity.IpoOrder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class CreateIpoOrderPublisherImpl implements CreateIpoOrderPublisher{
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final KafkaTemplate<String, String> kafkaTemplate;

    public CreateIpoOrderPublisherImpl(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public Mono<Boolean> publish(IpoOrder message) {
        return Mono.fromCallable(() -> {
            ListenableFuture<SendResult<String, String>> future = this.kafkaTemplate.send(MessageConstant.CREATE_IPO_ORDER,
                    objectMapper.writeValueAsString(message));

            return future.completable()
                    .thenApply(result -> true)
                    .exceptionally(ex -> false);
        }).flatMap(Mono::fromCompletionStage);
    }
}
