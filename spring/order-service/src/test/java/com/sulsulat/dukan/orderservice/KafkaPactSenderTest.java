package com.sulsulat.dukan.orderservice;


import au.com.dius.pact.provider.PactVerifyProvider;
import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.loader.PactFolder;
import au.com.dius.pact.provider.junit5.AmpqTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sulsulat.dukan.orderservice.kafka.MessageSender;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;



@Provider("OrderService")
@SpringBootTest
@PactFolder("../pacts")
public class KafkaPactSenderTest {

	ObjectMapper objectMapper = new ObjectMapper();
	Order order;
	
	@BeforeEach 
	void setupTestTarget(PactVerificationContext context) {
		context.setTarget(new AmpqTestTarget());
	}
	
	@TestTemplate
	@ExtendWith(PactVerificationInvocationContextProvider.class)
	void pactVerificationTestTemplate(PactVerificationContext context) {
		context.verifyInteraction();
	}
	
	@State("Order received") // Method will be run before testing interactions that require "default" or "no-data" state
    public void toDefaultState() {
        // Prepare service before interaction that require "default" state
		order = new Order();
		
		order.setCustomerEmail("user1@gmail.com");
		order.setOrderId(1L);
		order.setBooks(Arrays.asList("book1 title", "book2 title"));
	}
	
	@Autowired
	MessageSender sender;
	
	@PactVerifyProvider("order")
	public String providerKafkaMessage() throws Exception{
		
		return sender.generateOrderMessage(order);
		
	}
	
}

