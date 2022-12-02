package com.masy.telepasschallenge.mapper;

import com.masy.telepasschallenge.data.dto.CustomerDto;
import com.masy.telepasschallenge.data.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING, unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface CustomerMapper {

    CustomerDto mapToDto(Customer device);

    Customer mapToEntity(CustomerDto device);

}
