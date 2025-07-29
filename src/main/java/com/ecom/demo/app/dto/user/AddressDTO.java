package com.ecom.demo.app.dto.user;

import lombok.Data;

@Data
public class AddressDTO {
    private String city;
    private String state;
    private String zipcode;
    private String country;
}
