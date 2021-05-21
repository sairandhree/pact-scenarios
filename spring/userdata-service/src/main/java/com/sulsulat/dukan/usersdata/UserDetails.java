package com.sulsulat.dukan.usersdata;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@Entity 
public class UserDetails {
    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;
 
    private String name;

    private String email;
    private Long phonenumber;

    @OneToOne
    private Address address;
    
}