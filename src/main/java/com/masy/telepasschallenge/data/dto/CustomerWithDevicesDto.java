package com.masy.telepasschallenge.data.dto;

import lombok.Data;

import java.util.List;

@Data
public class CustomerWithDevicesDto {
    CustomerDto customer;
    List<DeviceDto> devices;
}
