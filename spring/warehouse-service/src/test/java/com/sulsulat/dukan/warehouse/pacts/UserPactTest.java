package com.sulsulat.dukan.warehouse.pacts;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import au.com.dius.pact.core.model.annotations.PactFolder;

import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.sulsulat.dukan.warehouse.User;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "UserDataProvider", port = "1234")
@PactFolder("../pacts")
public class UserPactTest {

    final String pactDslJson = new PactDslJsonBody()
    .stringMatcher("name", "^[a-zA-Z0-9_ ]*$","Foo")
    .stringMatcher("email", "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$", "bar@baz.com")
    .object("address")
        .stringMatcher("addrStr1", "^[a-zA-Z0-9_ ]*$", "addrStr1")
        .stringMatcher("addrStr2", "^[a-zA-Z0-9_ ]*$", "addrStr2")
        .stringMatcher("city", "^[a-zA-Z0-9_ ]*$", "pune")
        .integerType("pin", 411045)
    .closeObject()
    .getBody()
    .toString();

    @Pact(provider="UserDataProvider", consumer="WarehouseAddressConsumer")
    public RequestResponsePact createPact(PactDslWithProvider builder) {
        return builder
                .given("user with email bar@baz.com")
                .uponReceiving("response for user address")
                .path("/users/bar@baz.com")
                .method("GET")
                .willRespondWith()
                .status(200)
                .body(pactDslJson)
                .toPact();
    }

    @Test
    void test(MockServer mockServer) throws Exception {
        HttpResponse httpResponse = Request.Get(mockServer.getUrl() + "/users/bar@baz.com").execute().returnResponse();
        assertThat(httpResponse.getStatusLine().getStatusCode(), is(equalTo(200)));
        ObjectMapper mapper = new ObjectMapper();
        User userDto = mapper.readValue(pactDslJson, User.class);
        assertThat(userDto.getName(), is(equalTo(("Foo"))));
    }


}