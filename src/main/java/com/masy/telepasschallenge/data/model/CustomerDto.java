package com.masy.telepasschallenge.data.model;

import lombok.Data;

@Data
public class CustomerDto {

    Long id;
    String name;
    String surname;
    String fiscalCode;
    String address;

}
