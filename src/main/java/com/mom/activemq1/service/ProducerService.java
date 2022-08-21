package com.mom.activemq1.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mom.activemq1.entity.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class ProducerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProducerService.class);
    private final JmsTemplate topicJmsTemplate;
    private final JmsTemplate queueJmsTemplate;
    @Value("${activemq.topic-name}")
    private String destinationTopic;
    @Value("${activemq.queue-name}")
    private String destinationQueue;

    @Autowired
    public ProducerService(@Qualifier("topicJmsTemplate") JmsTemplate topicJmsTemplate,
                           @Qualifier("queueJmsTemplate") JmsTemplate queueJmsTemplate) {
        this.topicJmsTemplate = topicJmsTemplate;
        this.queueJmsTemplate = queueJmsTemplate;
    }

    public void sendToTopic() throws JsonProcessingException {
        int random = ThreadLocalRandom.current().nextInt(1, 1000);
        Order order = new Order("Phone",random);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonOrder = objectMapper.writeValueAsString(order);

        topicJmsTemplate.convertAndSend(destinationTopic, jsonOrder);
        LOGGER.info("send {} to topic {}", jsonOrder, destinationTopic);
    }

    public void sendToQueue() throws JsonProcessingException {
        int random = ThreadLocalRandom.current().nextInt(1, 1000);
        Order order = new Order("Phone",random);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonOrder = objectMapper.writeValueAsString(order);

        queueJmsTemplate.convertAndSend(destinationQueue, jsonOrder);
        LOGGER.info("send {} to queue {}", jsonOrder, destinationQueue);
    }


}
