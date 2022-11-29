package com.masy.telepasschallenge.service;

import com.masy.telepasschallenge.data.dto.DeviceDto;
import com.masy.telepasschallenge.data.dto.DeviceStatusDto;
import com.masy.telepasschallenge.data.model.Device;
import com.masy.telepasschallenge.exception.MaxDevicesToCustomerException;
import com.masy.telepasschallenge.exception.NotDeletedException;
import com.masy.telepasschallenge.exception.NotFoundException;
import com.masy.telepasschallenge.mapper.DeviceMapper;
import com.masy.telepasschallenge.repository.DeviceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.BDDAssertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class DeviceServiceImplTest {

    DeviceServiceImpl underTest;

    @Mock
    DeviceRepository deviceRepository;
    @Mock
    DeviceMapper deviceMapper;
    static final long MAX_DEVICES_PER_CUSTOMER = 2;

    @BeforeEach
    void setUp() {
        underTest = new DeviceServiceImpl(deviceRepository, deviceMapper);
        underTest.setMaxDevicesPerCustomer(MAX_DEVICES_PER_CUSTOMER);
    }

    @Test
    void findDevice_ThrowsNotFound() {
        // given
        UUID id = UUID.randomUUID();
        given(deviceRepository.findById(id))
                .willReturn(Optional.empty());

        // when
        Throwable actual = catchThrowable(() -> underTest.findDevice(id));

        // then
        then(actual).isInstanceOf(NotFoundException.class);
    }

    @Test
    void findDevice_Found() {
        // given
        UUID id = UUID.randomUUID();
        Device entity = new Device().setId(id);
        DeviceDto expected = new DeviceDto().setId(id);
        given(deviceRepository.findById(id))
                .willReturn(Optional.of(entity));
        given(deviceMapper.mapToDto(entity))
                .willReturn(expected);

        // when
        DeviceDto actual = underTest.findDevice(id);

        // then
        then(actual).isEqualTo(expected);
    }

    @Test
    void saveDevice_ThrowsMaxDevices() {

        // given
        DeviceDto dto = new DeviceDto().setCustomer(1L);
        given(deviceRepository.countByCustomerId(dto.getCustomer()))
                .willReturn(MAX_DEVICES_PER_CUSTOMER);

        // when
        Throwable actual = catchThrowable(() -> underTest.saveDevice(dto));

        // then
        then(actual).isInstanceOf(MaxDevicesToCustomerException.class);

    }

    @Test
    void saveDevice_Saved() {

        // given
        UUID expected = UUID.randomUUID();
        DeviceDto dto = new DeviceDto().setCustomer(1L);
        Device device = new Device().setStatus(Device.Status.ACTIVE);
        given(deviceRepository.countByCustomerId(dto.getCustomer()))
                .willReturn(0L);
        given(deviceMapper.mapToEntity(dto)).willReturn(device);
        given(deviceRepository.save(device)).willReturn(device.setId(expected));

        // when
        UUID actual = underTest.saveDevice(dto);

        // then
        then(actual).isEqualTo(expected);

    }

    @Test
    void deleteDevice_NotFound() {
        //given
        UUID id = UUID.randomUUID();
        given(deviceRepository.findById(id)).willReturn(Optional.empty());

        //when
        Throwable actual = catchThrowable(() -> underTest.deleteDevice(id));

        // then
        then(actual).isInstanceOf(NotDeletedException.class);

    }


    @Test
    void deleteDevice_Deleted() {
        //given
        UUID id = UUID.randomUUID();
        Device device = new Device().setId(id);
        given(deviceRepository.findById(id)).willReturn(Optional.of(device));

        //when
        underTest.deleteDevice(id);

        // then
        BDDMockito.then(deviceRepository).should().delete(device); // verify
    }

    @Test
    void updateStatus_NotFound() {
        //given
        UUID id = UUID.randomUUID();
        given(deviceRepository.findById(id)).willReturn(Optional.empty());

        //when
        Throwable actual = catchThrowable(() -> underTest.updateStatus(id, new DeviceStatusDto()));

        // then
        then(actual).isInstanceOf(NotFoundException.class);

    }

    @Test
    void updateStatus_Updated() {
        //given
        UUID id = UUID.randomUUID();
        Device.Status expected = Device.Status.ACTIVE;
        Device device = new Device().setId(id);
        DeviceStatusDto dto = new DeviceStatusDto().setNewStatus(expected);
        given(deviceRepository.findById(id)).willReturn(Optional.of(device));

        //when
        underTest.updateStatus(id, dto);

        // then
        then(device.getStatus()).isEqualTo(expected);
    }

}