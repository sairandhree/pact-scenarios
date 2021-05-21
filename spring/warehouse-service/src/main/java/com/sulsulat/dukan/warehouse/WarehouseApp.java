package com.sulsulat.dukan.warehouse;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class WarehouseApp {

	public static void main(String[] args) {
		SpringApplication.run(WarehouseApp.class, args);
	}

	// @Autowired
	// UserClient userClient;
	
	// @PostConstruct
	// public void getAddress() {
	// 	System.out.println("\n\n\n");
	// 	System.out.println(userClient.getUser("user1@gmail.com")	);
	// }
	
}
