package com.masy.telepasschallenge.repository;

import com.masy.telepasschallenge.data.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DeviceRepository extends JpaRepository<Device, UUID> {

    long countByCustomerId(Long customerId);
    List<Device> findByCustomerId(Long customerId);
}