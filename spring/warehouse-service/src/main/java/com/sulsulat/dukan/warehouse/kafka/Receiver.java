package com.sulsulat.dukan.warehouse.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sulsulat.dukan.warehouse.Order;
import com.sulsulat.dukan.warehouse.User;
import com.sulsulat.dukan.warehouse.UserClient;

import org.springframework.kafka.support.KafkaHeaders;

@Service
public class Receiver {

	private static final Logger LOGGER = LoggerFactory.getLogger(Receiver.class);

	private Boolean isWorkdone = false;

	ObjectMapper objectMapper = new ObjectMapper();

	public Receiver() {

	}

	public Boolean isWorkdone() {
		return isWorkdone;
	}

	@Autowired
	UserClient userClient;

	@KafkaListener(topics = "test", groupId = "group_id")
	public void consume(String payload) throws Exception {

		LOGGER.info("Order received.  Payload   is  {}", payload);
		Order order = validateMessage(payload);
		User userDetails = userClient.getUser(order.getCustomerEmail());

		LOGGER.info("Order shipped to {}", userDetails.getAddress());

		isWorkdone = true;

	}

	public Order validateMessage(String message) {

		Order orderReceived = new Order();
		try {
			orderReceived = objectMapper.readValue(message, Order.class);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		LOGGER.info("Order validated. Parsed order is  {}", orderReceived);
		return orderReceived;

	}
}
