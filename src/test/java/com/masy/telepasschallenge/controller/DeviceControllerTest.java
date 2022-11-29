package com.masy.telepasschallenge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.masy.telepasschallenge.data.dto.DeviceDto;
import com.masy.telepasschallenge.data.dto.DeviceStatusDto;
import com.masy.telepasschallenge.data.model.Device;
import com.masy.telepasschallenge.service.DeviceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(DeviceController.class)
class DeviceControllerTest {

    @MockBean
    private DeviceService deviceService;

    @Autowired
    private ObjectMapper om;

    @Autowired
    MockMvc mockMvc;


    @Test
    void updateStatus() throws Exception {
        // given
        UUID id = UUID.randomUUID();
        DeviceStatusDto dto = new DeviceStatusDto().setNewStatus(Device.Status.ACTIVE);

        // when
        ResultActions action = mockMvc.perform(
                patch("/devices/{id}", id)
                .content(om.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        action.andExpect(status().isNoContent());
        then(deviceService).should().updateStatus(id, dto);
    }

    @Test
    void getDevice() throws Exception {
        // given
        UUID id = UUID.randomUUID();
        DeviceDto expected = new DeviceDto().setId(id);
        given(deviceService.findDevice(id))
                .willReturn(expected);

        // when
        ResultActions action = mockMvc.perform(get("/devices/{id}", id));

        // then
        action.andExpect(status().isOk())
                .andExpect(content().json(om.writeValueAsString(expected)));

    }

    @Test
    void insertDevice() throws Exception {
        UUID expected = UUID.randomUUID();
        DeviceDto dto = new DeviceDto().setStatus(Device.Status.ACTIVE);
        given(deviceService.saveDevice(dto))
                .willReturn(expected);

        // when
        ResultActions action = mockMvc.perform(post("/devices")
                .content(om.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON));


        // then
        action.andExpect(status().isCreated())
                .andExpect(content().string(""))
                .andExpect(header().string("location", String.valueOf(expected)));
    }

    @Test
    void deleteDevice() throws Exception {
        // given
        UUID id = UUID.randomUUID();

        // when
        ResultActions action = mockMvc.perform(delete("/devices/{id}", id));

        // then
        action.andExpect(status().isNoContent());
        then(deviceService).should().deleteDevice(id);
    }
}








