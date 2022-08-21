package com.mom.activemq1.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mom.activemq1.entity.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerService.class);

    //selector - filter msg-s that consumer can ingest. The same feature as SNS filter by Attributes
    @JmsListener( destination = "${activemq.topic-name}", subscription = "topic-order-consumer", selector = "test=false OR test is null",
            containerFactory="topicListenerFactory")
    public void listenTopic(String msg) {
        LOGGER.info("incoming topic msg {}", msg);
        Order order = new Order();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            order = objectMapper.readValue(msg, Order.class);
            LOGGER.info("converted msg {}", order);
        }catch(Exception e){
            LOGGER.error(e.getMessage());
        }
    }

    @JmsListener( destination = "${activemq.queue-name}", subscription = "queue-order-consumer",
            containerFactory="queueListenerFactory")
    public void listenQueue(String msg) {
        LOGGER.info("incoming queue msg {}", msg);
        Order order = new Order();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            order = objectMapper.readValue(msg, Order.class);
            LOGGER.info("converted msg {}", order);
        }catch(Exception e){
            LOGGER.error(e.getMessage());
        }
    }
}
