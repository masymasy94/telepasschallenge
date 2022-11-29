package com.masy.telepasschallenge.mapper;

import com.masy.telepasschallenge.data.dto.DeviceDto;
import com.masy.telepasschallenge.data.model.Customer;
import com.masy.telepasschallenge.data.model.Device;
import com.masy.telepasschallenge.exception.NotFoundException;
import com.masy.telepasschallenge.repository.CustomerRepository;
import lombok.Setter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING, unmappedTargetPolicy = ReportingPolicy.ERROR)
public abstract class DeviceMapper {

    @Setter(onMethod_ = @Autowired)
    private CustomerRepository customerRepository;

    @Mapping(target = "customer", source = "customer.id")
    public abstract DeviceDto mapToDto(Device device);

    public abstract Device mapToEntity(DeviceDto device);

    protected Customer getCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(NotFoundException::new);
    }

}
