package com.masy.telepasschallenge.mapper;

import com.masy.telepasschallenge.data.model.Customer;
import com.masy.telepasschallenge.data.model.CustomerDto;
import com.masy.telepasschallenge.data.model.Device;
import com.masy.telepasschallenge.data.model.DeviceDto;
import com.masy.telepasschallenge.exception.NotFoundException;
import com.masy.telepasschallenge.repository.CustomerRepository;
import lombok.Setter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING, unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface CustomerMapper {

    CustomerDto mapToDto(Customer device);

    Customer mapToEntity(CustomerDto device);

}
