package com.sulsulat.dukan.usersdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserRepository userRepo;
    @GetMapping("/{email}")
    public UserDetails getUser(@PathVariable String email){
        UserDetails user = userRepo.findByEmail(email);
        return user;
    }
}