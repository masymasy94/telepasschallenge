package com.masy.telepasschallenge.controller;

import com.masy.telepasschallenge.data.dto.CustomerAddressDto;
import com.masy.telepasschallenge.data.dto.CustomerDto;
import com.masy.telepasschallenge.data.dto.CustomerWithDevicesDto;
import com.masy.telepasschallenge.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@Validated
@RequestMapping(value = "customers", produces = MediaType.APPLICATION_JSON_VALUE)
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("{id}")
    public CustomerWithDevicesDto getCustomer(@PathVariable Long id) {
        return customerService.findCustomer(id);

    }


    @PatchMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateAddress(@PathVariable Long id, @RequestBody @Valid CustomerAddressDto dto){

        customerService.updateAddress(id, dto);
    }


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> insertCustomer(@RequestBody CustomerDto dto) {

        Long id = customerService.saveCustomer(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("location", String.valueOf(id))
                .build();
    }


    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // or 404
    public void deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
    }

}
