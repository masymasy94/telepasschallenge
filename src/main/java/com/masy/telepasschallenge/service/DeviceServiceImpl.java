package com.masy.telepasschallenge.service;

import com.masy.telepasschallenge.data.dto.DeviceDto;
import com.masy.telepasschallenge.data.dto.DeviceStatusDto;
import com.masy.telepasschallenge.data.model.Device;
import com.masy.telepasschallenge.exception.MaxDevicesToCustomerException;
import com.masy.telepasschallenge.exception.NotDeletedException;
import com.masy.telepasschallenge.exception.NotFoundException;
import com.masy.telepasschallenge.mapper.DeviceMapper;
import com.masy.telepasschallenge.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;
    private final DeviceMapper deviceMapper;

    @Setter
    @Value("${telepass.max-devices-per-customer}")
    long maxDevicesPerCustomer;


    @Override
    @Transactional(readOnly = true)
    public DeviceDto findDevice(UUID id) {

        return deviceRepository.findById(id)
                .map(deviceMapper::mapToDto)
                .orElseThrow(NotFoundException::new);
    }


    @Override
    public UUID saveDevice(DeviceDto dto) {

        if (deviceRepository.countByCustomerId(dto.getCustomer()) < maxDevicesPerCustomer) {

            Device saved = deviceRepository.save(deviceMapper.mapToEntity(dto));
            return saved.getId();
        }
        throw new MaxDevicesToCustomerException(dto.getCustomer());
    }


    @Override
    public void deleteDevice(UUID id) {

        deviceRepository.findById(id).ifPresentOrElse(
                deviceRepository::delete,
                () -> {throw new NotDeletedException();}
        );
    }


    @Override
    public void updateStatus(UUID id, DeviceStatusDto dto) {

        deviceRepository.findById(id).ifPresentOrElse(
                device -> device.setStatus(dto.getNewStatus()),
                () -> {throw new NotFoundException();}
        );
    }
}
