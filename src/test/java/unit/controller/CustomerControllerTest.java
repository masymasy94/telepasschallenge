package unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.masy.telepasschallenge.controller.CustomerController;
import com.masy.telepasschallenge.data.dto.CustomerAddressDto;
import com.masy.telepasschallenge.data.dto.CustomerDto;
import com.masy.telepasschallenge.data.dto.CustomerWithDevicesDto;
import com.masy.telepasschallenge.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import util.TelepassSpringTest;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TelepassSpringTest
class CustomerControllerTest {
    @MockBean
    private CustomerService customerService;

    @Autowired
    private ObjectMapper om;

    @Autowired
    MockMvc mockMvc;

    @Test
    void getCustomer() throws Exception {
        // given
        Long id = 1L;
        CustomerDto customer = new CustomerDto().setId(id);
        CustomerWithDevicesDto expected = new CustomerWithDevicesDto().setCustomer(customer);
        given(customerService.findCustomer(id))
                .willReturn(expected);

        // when
        ResultActions action = mockMvc.perform(get("/customers/{id}", id));

        // then
        action.andExpect(status().isOk())
                .andExpect(content().json(om.writeValueAsString(expected)));
    }

    @Test
    void updateAddress() throws Exception {
        // given
        Long id = 1L;
        CustomerAddressDto dto = new CustomerAddressDto().setNewAddress("New Address!");

        // when
        ResultActions action = mockMvc.perform(
                patch("/customers/{id}", id)
                        .content(om.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        action.andExpect(status().isNoContent());
        then(customerService).should().updateAddress(id, dto);
    }

    @Test
    void insertCustomer() throws Exception {
        Long expected = 1L;
        CustomerDto dto = new CustomerDto().setName("Andrea");
        given(customerService.saveCustomer(dto))
                .willReturn(expected);

        // when
        ResultActions action = mockMvc.perform(post("/customers")
                .content(om.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON));


        // then
        action.andExpect(status().isCreated())
                .andExpect(content().string(""))
                .andExpect(header().string("location", String.valueOf(expected)));
    }

    @Test
    void deleteCustomer() throws Exception {
        // given
        Long id = 1L;

        // when
        ResultActions action = mockMvc.perform(delete("/customers/{id}", id));

        // then
        action.andExpect(status().isNoContent());
        then(customerService).should().deleteCustomer(id);
    }
}