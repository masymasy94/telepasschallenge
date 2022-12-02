package integration;

import com.masy.telepasschallenge.data.dto.CustomerAddressDto;
import com.masy.telepasschallenge.data.dto.CustomerDto;
import com.masy.telepasschallenge.data.dto.CustomerWithDevicesDto;
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

import static org.assertj.core.api.Assertions.assertThat;

@TelepassSpringTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerIntegrationTest {

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
    public void testCustomerCycle() {
        CustomerDto customer = new CustomerDto()
                .setName("Andrea")
                .setSurname("Masiero")
                .setAddress("Via Fasulla 123");

        ResponseEntity<String> responseEntityInsert = this.restTemplate
                .postForEntity(BASE_URL + port + "/customers", customer, String.class);

        assertThat(responseEntityInsert.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntityInsert.getHeaders().get("location")).size().isNotZero();

//---------------------------------------------------------------------------------------


        String id = new String(responseEntityInsert.getHeaders().get("location").get(0).getBytes(StandardCharsets.UTF_8));

        ResponseEntity<CustomerWithDevicesDto> responseEntityGet =  restTemplate.getForEntity(BASE_URL + port + "/customers/"+ id, CustomerWithDevicesDto.class);

        assertThat(responseEntityGet.getStatusCode()).isEqualTo(HttpStatus.OK);

//---------------------------------------------------------------------------------------

        CustomerAddressDto newAddress = new CustomerAddressDto().setNewAddress("Via telepass 456");

        ResponseEntity<Void> responseEntityPatch = restTemplate.exchange(BASE_URL + port + "/customers/"+ id ,
                HttpMethod.PATCH, new HttpEntity<>(newAddress), Void.class);

        assertThat(responseEntityPatch.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<CustomerWithDevicesDto> responseEntityGetAfterPatch =  restTemplate.getForEntity(BASE_URL + port + "/customers/"+ id, CustomerWithDevicesDto.class);
        assertThat(responseEntityGetAfterPatch.getBody()).isNotNull();
        assertThat(responseEntityGetAfterPatch.getBody().getCustomer().getAddress()).isEqualTo(newAddress.getNewAddress());

//---------------------------------------------------------------------------------------

        ResponseEntity<Void> responseEntityDelete = restTemplate.exchange(BASE_URL + port + "/customers/"+ id ,
                HttpMethod.DELETE, new HttpEntity<>(null), Void.class);

        assertThat(responseEntityDelete.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<CustomerWithDevicesDto> responseEntityGetAfterDelete =  restTemplate.getForEntity(BASE_URL + port + "/customers/"+ id, CustomerWithDevicesDto.class);
        assertThat(responseEntityGetAfterDelete.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);


    }




}
