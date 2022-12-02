package com.masy.telepasschallenge.data.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CustomerAddressDto {

    @NotNull
    String newAddress;
}
