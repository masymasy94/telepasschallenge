package com.masy.telepasschallenge.controller;

import com.masy.telepasschallenge.data.dto.DeviceDto;
import com.masy.telepasschallenge.data.dto.DeviceStatusDto;
import com.masy.telepasschallenge.service.DeviceService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@AllArgsConstructor
@Validated
@RequestMapping(value = "devices", produces = MediaType.APPLICATION_JSON_VALUE)
public class DeviceController {


    private final DeviceService deviceService;


    @PatchMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateStatus(@PathVariable UUID id, @RequestBody @Valid DeviceStatusDto dto) {
        deviceService.updateStatus(id, dto);
    }


    @GetMapping("{id}")
    public DeviceDto getDevice(@PathVariable UUID id) {
        return deviceService.findDevice(id);

    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> insertDevice(@RequestBody DeviceDto dto) {

        UUID id = deviceService.saveDevice(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("location", String.valueOf(id))
                .build();
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // or 404
    public void deleteDevice(@PathVariable UUID id) {
        deviceService.deleteDevice(id);
    }
}
