package com.sulsulat.dukan.usersdata;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
public class Address {
    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;
    private String addrStr1;
    private String addrStr2;
    private String city;
    private Integer pin;

}
