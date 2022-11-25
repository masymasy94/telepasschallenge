package com.masy.telepasschallenge.data.model;

import lombok.Data;

import java.util.UUID;

@Data
public class DeviceDto {

    UUID id;
    Device.Status status;
    Long customer;

}
