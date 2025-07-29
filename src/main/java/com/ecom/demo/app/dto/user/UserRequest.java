package com.ecom.demo.app.dto.user;

import lombok.Data;

@Data
public class UserRequest {
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String phone;
    private final AddressDTO address;

    public UserRequest(String firstName, String lastName, String email, String phone, AddressDTO address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }
}
