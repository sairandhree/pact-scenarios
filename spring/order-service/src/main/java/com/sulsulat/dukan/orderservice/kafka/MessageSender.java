package com.sulsulat.dukan.orderservice.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sulsulat.dukan.orderservice.Order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageSender.class);
    private static final String TOPIC = "test";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void send(String payload) {
        LOGGER.info("sending payload='{}'", payload);
        kafkaTemplate.send(TOPIC, "update",  payload);
      }
    
      public void send(Order order) {
        try {
          LOGGER.info("sending payload='{}'", generateOrderMessage(order));
          kafkaTemplate.send(TOPIC, "update",  generateOrderMessage(order));
        } catch (JsonProcessingException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        
      }
    
      public String generateOrderMessage(Order order) throws JsonProcessingException{
        ObjectMapper objectMapper =new ObjectMapper();
    
        String stringMessage = objectMapper.writeValueAsString(order);
            
            return stringMessage;
    
      }
}