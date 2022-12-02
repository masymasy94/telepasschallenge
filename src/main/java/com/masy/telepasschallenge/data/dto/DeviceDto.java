package com.masy.telepasschallenge.data.dto;

import com.masy.telepasschallenge.data.model.Device;
import lombok.Data;

import java.util.UUID;

@Data
public class DeviceDto {

    UUID id;
    Device.Status status;
    Long customer;

}
