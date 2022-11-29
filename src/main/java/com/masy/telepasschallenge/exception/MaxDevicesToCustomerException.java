package com.masy.telepasschallenge.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MaxDevicesToCustomerException extends RuntimeException {
    private final Long customerId;

}
