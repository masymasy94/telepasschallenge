package unit.controller.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.masy.telepasschallenge.TelepassChallengeApplication;
import com.masy.telepasschallenge.data.dto.ResponseDto;
import com.masy.telepasschallenge.exception.MaxDevicesToCustomerException;
import com.masy.telepasschallenge.exception.NotFoundException;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import util.TelepassSpringTest;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import static org.assertj.core.api.BDDAssertions.then;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@TelepassSpringTest({TelepassChallengeApplication.class, RestExceptionHandlerTest.Config.TestController.class})
public class RestExceptionHandlerTest {


    @Autowired
    private ObjectMapper mapper;

    @Autowired
    MockMvc mockMvc;



    @Test
    void handle_uncaughtException() throws Exception {
        // given
        ResponseDto<Void> expected = ResponseDto.<Void>builder()
                .message("Unexpected error: test message 2")
                .build();

        // when
        final ResultActions action = mockMvc.perform(get("/test/uncaught"));

        // then
        action
                .andExpect(status().isInternalServerError())
                .andExpect(content().json(mapper.writeValueAsString(expected)));
    }


    @Test
    public void handle_NotFoundException() throws Exception {
        // given

        // when
        ResultActions action = mockMvc.perform(get("/test/db")
                .queryParam("anotherParam", "1"));

        // then
        action.andExpect(status().isNotFound());
    }


    @Test
    public void handle_MaxDevicesToCustomerException() throws Exception {
        // given

        // when
        ResultActions action = mockMvc.perform(get("/test/max"));

        // then
        action.andExpect(status().isBadRequest());
    }


    @Test
    void handle_MethodArgumentTypeMismatchException() throws Exception {
        // given

        // when
        ResultActions action = mockMvc.perform(get("/test/validation")
                .queryParam("param", "value")
                .queryParam("anotherParam", "notANumber"));
        String actualString = action.andReturn().getResponse().getContentAsString();

        // then
        action.andExpect(status().isBadRequest())
                .andExpect(content().json(actualString));
    }

    @Test
    void handle_HttpMessageNotReadableException() throws Exception {
        // given
        String invalidBody = "{\"field\": 42";
        String expected = "Could not parse body";

        // when
        ResultActions action = mockMvc.perform(post("/test/validation")
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(invalidBody)));
        String actualString = action.andReturn().getResponse().getContentAsString();
        String responseMessage = mapper.readValue(actualString, ResponseDto.class).getMessage();

        // then
        action.andExpect(status().isBadRequest());
        then(responseMessage.contains(expected)).isTrue();
    }


    @Test
    void handle_BindException() throws Exception {
        // given
        Body body = Body.builder()
                .field(null)
                .build();
        String expected = "Validation failed: ";

        // when
        ResultActions action = mockMvc.perform(post("/test/validation")
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(body)));
        String actualString = action.andReturn().getResponse().getContentAsString();

        // then
        action
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON));
        then(actualString.contains(expected)).isTrue();
    }


    @Test
    void handle_MissingServletRequestParameterException() throws Exception {
        // given
        String expected = "required request parameter";
                
        // when
        ResultActions action = mockMvc.perform(get("/test/validation")
                .queryParam("anotherParam", "1"));
        String actualString = action.andReturn().getResponse().getContentAsString();

        // then
        action
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON));
        then(actualString.contains(expected)).isTrue();
    }


    @Test
    void handle_ConstraintValidationException() throws Exception {
        // given
        String expected = "Validation failed: ";

        // when
        ResultActions action = mockMvc.perform(get("/test/validation")
                        .queryParam("param", "")
                        .queryParam("anotherParam", "-1"));
        String actualString = action.andReturn().getResponse().getContentAsString();

        // then
        action
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON));
        then(actualString.contains(expected)).isTrue();

    }



    @Configuration
    @SuppressWarnings("unused")
    static class Config {

        @Validated
        @RestController
        @RequiredArgsConstructor
        @RequestMapping(path = "/test")
        static class TestController {
            @GetMapping("/validation")
            int validation(
                    @RequestParam @NotBlank String param,
                    @RequestParam @Positive int anotherParam
            ) {
                return 42;
            }

            @PostMapping(path = "/validation", consumes = APPLICATION_JSON_VALUE)
            long bodyValidation(@RequestBody @Valid Body body) {
                return 42;
            }

            @GetMapping("/db")
            String db() {
                throw new NotFoundException();
            }

            @GetMapping("/uncaught")
            String uncaught() {
                throw new IllegalStateException("test message 2");
            }

            @GetMapping("/max")
            String maxDevices() {
                throw new MaxDevicesToCustomerException(1L);
            }

        }

    }

    @Value
    @Builder
    @Jacksonized
    static class Body {
        @NotBlank String field;
    }

}
