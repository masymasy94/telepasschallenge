package com.masy.telepasschallenge.service;

import com.masy.telepasschallenge.data.dto.CustomerAddressDto;
import com.masy.telepasschallenge.data.dto.CustomerDto;
import com.masy.telepasschallenge.data.dto.CustomerWithDevicesDto;

public interface CustomerService {
    CustomerWithDevicesDto findCustomer(Long id);

    void updateAddress(Long id, CustomerAddressDto dto);

    Long saveCustomer(CustomerDto dto);

    void deleteCustomer(Long id);
}
