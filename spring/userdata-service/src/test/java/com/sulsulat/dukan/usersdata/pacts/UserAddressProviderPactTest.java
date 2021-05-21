package com.sulsulat.dukan.usersdata.pacts;

import com.sulsulat.dukan.usersdata.Address;
import com.sulsulat.dukan.usersdata.UserDetails;
import com.sulsulat.dukan.usersdata.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import static org.mockito.ArgumentMatchers.any;
import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.loader.PactFolder;
import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import static org.mockito.Mockito.when;
@ExtendWith(PactVerificationInvocationContextProvider.class)
@Provider("UserAddressProvider")
@PactFolder("../pacts")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserAddressProviderPactTest {

    @State("user with email bar@baz.com")
    public void userExists() {
        System.out.println("user with email bar@baz.com");
        UserDetails user = new UserDetails();
        user.setName("Foo");
        user.setEmail("bar@baz.com");
        Address address = new Address();
        address.setAddrStr1("addrStr1");
        address.setAddrStr2("addrStr2");
        address.setCity("pune");
        address.setPin(411045);
        user.setAddress(address);
        when(userRepository.findByEmail(any())).thenReturn(user);
    }

    @LocalServerPort
    private int port;

     @MockBean
     private UserRepository userRepository;

    @BeforeEach
    void before(PactVerificationContext context) {
        context.setTarget(new HttpTestTarget("localhost", port));
    }

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void pactVerificationTestTemplate(PactVerificationContext context) {
        context.verifyInteraction();
    }

}