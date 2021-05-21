package com.sulsulat.dukan.orderservice;

import java.util.List;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class Order {

    Long orderId;

    List<String> books;

    String customerEmail;
    
}