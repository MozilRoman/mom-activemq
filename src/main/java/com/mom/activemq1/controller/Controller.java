package com.mom.activemq1.controller;

import com.mom.activemq1.service.ProducerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class Controller {
    private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);
    private ProducerService producerService;

    @Autowired
    public Controller(ProducerService producerService) {
        this.producerService = producerService;
    }

    @GetMapping("/produceTopic")
    public ResponseEntity<String> produceToTopic() {
        try {
            LOGGER.info("preparing msg");
            producerService.sendToTopic();
            return new ResponseEntity<>("Produced ADD messages successfully", HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("ADD messages did not delivered", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/produceQueue")
    public ResponseEntity<String> produceToQueue() {
        try {
            LOGGER.info("preparing msg");
            producerService.sendToQueue();
            return new ResponseEntity<>("Produced ADD messages successfully", HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("ADD messages did not delivered", HttpStatus.BAD_REQUEST);
        }
    }

}
