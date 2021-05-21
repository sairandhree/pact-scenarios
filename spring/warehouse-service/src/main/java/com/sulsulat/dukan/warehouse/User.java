package com.sulsulat.dukan.warehouse;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter @Setter 
public class User {
	 private String name;
    private String email;
    private Long phonenumber;    
    private Address address;

}

@Getter @Setter 
@ToString
class Address{
	  private String addrStr1;
	    private String addrStr2;
	    private String city;
	    private Integer pin;
}