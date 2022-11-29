package com.masy.telepasschallenge.data.dto;

import com.masy.telepasschallenge.data.model.Device;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DeviceStatusDto {

    @NotNull
    Device.Status newStatus;
}
