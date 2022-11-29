package com.masy.telepasschallenge.service;

import com.masy.telepasschallenge.data.dto.DeviceDto;
import com.masy.telepasschallenge.data.dto.DeviceStatusDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface DeviceService {
    @Transactional(readOnly = true)
    DeviceDto findDevice(UUID id);

    UUID saveDevice(DeviceDto dto);

    void deleteDevice(UUID id);

    void updateStatus(UUID id, DeviceStatusDto dto);
}
