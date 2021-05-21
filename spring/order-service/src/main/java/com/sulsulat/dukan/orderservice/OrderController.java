package com.sulsulat.dukan.orderservice;

import com.sulsulat.dukan.orderservice.kafka.MessageSender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController{

    @Autowired
	MessageSender messageSender;

@PostMapping("/")
public String postOrder(@RequestBody Order orderToBeCreated){

    //Validate user
    //validate payment
    //Send the order to warehouse for shipping

    messageSender.send(orderToBeCreated);

    return orderToBeCreated.toString();

}


}