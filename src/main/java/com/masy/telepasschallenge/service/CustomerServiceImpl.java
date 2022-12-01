package com.masy.telepasschallenge.service;

import com.masy.telepasschallenge.data.dto.CustomerAddressDto;
import com.masy.telepasschallenge.data.dto.CustomerDto;
import com.masy.telepasschallenge.data.dto.CustomerWithDvicesDto;
import com.masy.telepasschallenge.data.dto.DeviceDto;
import com.masy.telepasschallenge.exception.NotDeletedException;
import com.masy.telepasschallenge.exception.NotFoundException;
import com.masy.telepasschallenge.exception.NotUpdatedException;
import com.masy.telepasschallenge.mapper.CustomerMapper;
import com.masy.telepasschallenge.mapper.DeviceMapper;
import com.masy.telepasschallenge.repository.CustomerRepository;
import com.masy.telepasschallenge.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final DeviceRepository deviceRepository;
    private final CustomerMapper customerMapper;
    private final DeviceMapper deviceMapper;

    @Override
    @Transactional(readOnly = true)
    public CustomerWithDvicesDto findCustomer(Long id) {

        CustomerDto customer = customerRepository.findById(id)
                .map(customerMapper::mapToDto)
                .orElseThrow(NotFoundException::new);

        List<DeviceDto> customerDevices = deviceRepository
                .findByCustomerId(id).stream()
                .map(deviceMapper::mapToDto)
                .toList();


        return new CustomerWithDvicesDto()
                .setCustomer(customer)
                .setDevices(customerDevices);
    }

    @Override
    public void updateAddress(Long id, CustomerAddressDto dto) {
        customerRepository.findById(id).ifPresentOrElse(
                customer -> customer.setAddress(dto.getNewAddress()),
                () ->  {throw new NotUpdatedException();}
        );
    }

    @Override
    public Long saveCustomer(CustomerDto dto) {
        return customerRepository
                .save(customerMapper.mapToEntity(dto))
                .getId();
    }

    @Override
    public void deleteCustomer(Long id) {
        customerRepository.findById(id).ifPresentOrElse(
                customerRepository::delete,
                () -> {throw new NotDeletedException();}
        );
    }
}
