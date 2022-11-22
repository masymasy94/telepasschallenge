package com.masy.telepasschallenge.repository;

import com.masy.telepasschallenge.data.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeviceRepository extends JpaRepository<Device, UUID> {
}
