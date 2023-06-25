package com.shafiya.fileparser.config;

import com.shafiya.fileparser.constant.MessageConstant;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Autowired
    private KafkaProperties kafkaProperties;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory(kafkaProperties));
        factory.setConcurrency(kafkaProperties.getListener().getConcurrency());
        factory.getContainerProperties().setDeliveryAttemptHeader(true);
        factory.setCommonErrorHandler(errorHandler(kafkaProperties));
        return factory;
    }

    public ConsumerFactory<String, String> consumerFactory(KafkaProperties kafkaProperties) {
        return new DefaultKafkaConsumerFactory<>(
                kafkaProperties.buildConsumerProperties());
    }

    DefaultErrorHandler errorHandler(KafkaProperties kafkaProperties) {
        int interval = Integer.parseInt(
                kafkaProperties.getConsumer().getProperties().get("retry.interval.ms"));
        int maxAttempts = Integer.parseInt(
                kafkaProperties.getConsumer().getProperties().get("retry.max.attempts"));
        FixedBackOff fixedBackOff = new FixedBackOff(interval, maxAttempts);
        DeadLetterPublishingRecoverer deadLetterPublishingRecoverer = new DeadLetterPublishingRecoverer(
                new KafkaTemplate<>(producerFactory(kafkaProperties)), this::getDltTopicPartition);
        return new DefaultErrorHandler(deadLetterPublishingRecoverer, fixedBackOff);
    }

    public ProducerFactory<String, String> producerFactory(KafkaProperties kafkaProperties) {
        return new DefaultKafkaProducerFactory<>(
                kafkaProperties.buildProducerProperties());
    }

    private TopicPartition getDltTopicPartition(ConsumerRecord<?, ?> record, Exception err) {
        return new TopicPartition(MessageConstant.CREATE_IPO_ORDER_DLT, 0);
    }
}
