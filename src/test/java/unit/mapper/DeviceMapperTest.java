package unit.mapper;

import com.masy.telepasschallenge.data.dto.DeviceDto;
import com.masy.telepasschallenge.data.model.Customer;
import com.masy.telepasschallenge.data.model.Device;
import com.masy.telepasschallenge.mapper.DeviceMapper;
import com.masy.telepasschallenge.mapper.DeviceMapperImpl;
import com.masy.telepasschallenge.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeviceMapperTest {

    @Mock
    CustomerRepository customerRepository;

    DeviceMapper underTest;

    @BeforeEach
    void setUp() {
        underTest = new DeviceMapperImpl();
        underTest.setCustomerRepository(customerRepository);
    }

    @Test
    void mapToEntity() {

        //given
        UUID deviceId = UUID.randomUUID();
        Customer customer = new Customer().setId(1L);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        DeviceDto dto = new DeviceDto().setCustomer(1L).setStatus(Device.Status.ACTIVE).setId(deviceId);
        Device expected = new Device().setCustomer(customer).setStatus(Device.Status.ACTIVE).setId(deviceId);

        //when
        Device actual = underTest.mapToEntity(dto);

        //then
        then(actual).usingRecursiveComparison().isEqualTo(expected);
    }
}