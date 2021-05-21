package com.sulsulat.dukan.warehouse.pacts;





import java.util.HashMap;

import java.util.Map;

import com.sulsulat.dukan.warehouse.kafka.Receiver;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;

import au.com.dius.pact.consumer.MessagePactBuilder;
import au.com.dius.pact.core.model.annotations.Pact;
import au.com.dius.pact.core.model.annotations.PactFolder;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslJsonRootValue;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.consumer.junit5.ProviderType;

import au.com.dius.pact.core.model.messaging.MessagePact;




@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "OrderService", providerType = ProviderType.ASYNCH)
@PactFolder("../pacts")
public class OrderPactTest {

	@Pact(consumer = "WarehouseOrderConsumer")
	MessagePact reciveMessagePact(MessagePactBuilder builder) {
		PactDslJsonBody body = new PactDslJsonBody();
		body
		.numberType("orderId", 42L)
        .stringType("customerEmail", "user2@gmail.com")
        .minArrayLike("books",1,PactDslJsonRootValue.stringMatcher("[^\n]+", "sample book name"))
		    
		.closeObject();

		Map<String, Object> metadata = new HashMap();
		metadata.put("key1", "value1");
		metadata.put("key2", "value2");

		MessagePact pact =  builder.given("Order received")
				.expectsToReceive("order")
				.withMetadata(metadata)
				.withContent(body)	
				.toPact();
		return pact;
	}

	@Test
	@PactTestFor(pactMethod = "reciveMessagePact")
	public void verifyOrderPact(MessagePact message) throws Exception {
	
		Receiver receiver = new Receiver();
		receiver.validateMessage(message.getMessages().get(0).getContents().valueAsString());

	}

}


