package integration;

import com.masy.telepasschallenge.data.dto.*;
import com.masy.telepasschallenge.data.model.Device;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import util.TelepassSpringTest;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@TelepassSpringTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DeviceIntegrationTest {

    @LocalServerPort
    private int port;

    private final String BASE_URL = "http://localhost:";

    @Autowired
    private TestRestTemplate restTemplate;



    @BeforeEach
    void setUp() {
        // this let restTemplate do PATCH calls
        restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    @Test
    public void testDeviceCycle() {

        CustomerDto customer = new CustomerDto()
                .setName("Andrea")
                .setSurname("Masiero")
                .setAddress("Via Fasulla 123");
        ResponseEntity<String> responseEntityCustomerInsert = this.restTemplate
                .postForEntity(BASE_URL + port + "/customers", customer, String.class);
        assertThat(responseEntityCustomerInsert.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntityCustomerInsert.getHeaders().get("location")).size().isNotZero();
        String idCustomer = new String(responseEntityCustomerInsert.getHeaders().get("location").get(0).getBytes(StandardCharsets.UTF_8));

        DeviceDto device = new DeviceDto().setCustomer(Long.valueOf(idCustomer)).setStatus(Device.Status.INACTIVE);

        ResponseEntity<String> responseEntityInsert = this.restTemplate
                .postForEntity(BASE_URL + port + "/devices", device, String.class);

        assertThat(responseEntityInsert.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntityInsert.getHeaders().get("location")).size().isNotZero();

//---------------------------------------------------------------------------------------

        String id = new String(responseEntityInsert.getHeaders().get("location").get(0).getBytes(StandardCharsets.UTF_8));

        ResponseEntity<DeviceDto> responseEntityGet =  restTemplate.getForEntity(BASE_URL + port + "/devices/"+ id, DeviceDto.class);

        assertThat(responseEntityGet.getStatusCode()).isEqualTo(HttpStatus.OK);

//---------------------------------------------------------------------------------------

        DeviceStatusDto newStatus = new DeviceStatusDto().setNewStatus(Device.Status.ACTIVE);

        ResponseEntity<Void> responseEntityPatch = restTemplate.exchange(BASE_URL + port + "/devices/"+ id ,
                HttpMethod.PATCH, new HttpEntity<>(newStatus), Void.class);

        assertThat(responseEntityPatch.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<DeviceDto> responseEntityGetAfterPatch =  restTemplate.getForEntity(BASE_URL + port + "/devices/"+ id, DeviceDto.class);
        assertThat(responseEntityGetAfterPatch.getBody()).isNotNull();
        assertThat(responseEntityGetAfterPatch.getBody().getStatus()).isEqualTo(newStatus.getNewStatus());

//---------------------------------------------------------------------------------------

        ResponseEntity<Void> responseEntityDelete = restTemplate.exchange(BASE_URL + port + "/devices/"+ id ,
                HttpMethod.DELETE, new HttpEntity<>(null), Void.class);

        assertThat(responseEntityDelete.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<DeviceDto> responseEntityGetAfterDelete =  restTemplate.getForEntity(BASE_URL + port + "/devices/"+ id, DeviceDto.class);
        assertThat(responseEntityGetAfterDelete.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }
}
