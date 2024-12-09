package com.example.commercemanager.dto;

import lombok.Data;

@Data
public class CustomerDTO {

    private String firstName;
    private String lastName;
    private String email;


    public CustomerDTO(String firstName, String lastName, String email) {
    }
}
