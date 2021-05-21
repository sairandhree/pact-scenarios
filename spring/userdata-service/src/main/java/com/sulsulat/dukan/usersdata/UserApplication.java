package com.sulsulat.dukan.usersdata;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserApplication.class, args);
	}

	@Autowired
	UserRepository userRepo;
	
	@Autowired
	AddressRepository addressRepo;
	
	@PostConstruct
	public void addUsers(){
		UserDetails user1 = new UserDetails();
		Address user1Address = new Address();

		user1Address.setAddrStr1(" flat no 12");
		user1Address.setAddrStr2("balewadi");
		user1Address.setCity("Pune");
		user1Address.setPin(411045);
		addressRepo.save(user1Address);
		
		user1.setAddress(user1Address);
		user1.setEmail("user1@gmail.com");
		user1.setName("user1name");
		userRepo.save(user1);

		UserDetails user2 = new UserDetails();
		Address user2Address = new Address();

		user2Address.setAddrStr1(" flat no 21");
		user2Address.setAddrStr2("Baner");
		user2Address.setCity("Pune");
		user2Address.setPin(411045);
		addressRepo.save(user2Address);
		
		user2.setAddress(user2Address);
		user2.setEmail("user2@gmail.com");
		user2.setName("user2 name");
		userRepo.save(user2);

		
	}

}
