package unit.service;

import com.masy.telepasschallenge.data.dto.CustomerAddressDto;
import com.masy.telepasschallenge.data.dto.CustomerDto;
import com.masy.telepasschallenge.data.dto.CustomerWithDevicesDto;
import com.masy.telepasschallenge.data.dto.DeviceDto;
import com.masy.telepasschallenge.data.model.Customer;
import com.masy.telepasschallenge.data.model.Device;
import com.masy.telepasschallenge.exception.NotFoundException;
import com.masy.telepasschallenge.mapper.CustomerMapper;
import com.masy.telepasschallenge.mapper.DeviceMapper;
import com.masy.telepasschallenge.repository.CustomerRepository;
import com.masy.telepasschallenge.repository.DeviceRepository;
import com.masy.telepasschallenge.service.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    CustomerServiceImpl underTest;

    @Mock
    CustomerRepository customerRepository;
    @Mock
    DeviceRepository deviceRepository;
    @Mock
    CustomerMapper customerMapper;
    @Mock
    DeviceMapper deviceMapper;


    @BeforeEach
    void setUp() {
        underTest = new CustomerServiceImpl(
                customerRepository,
                deviceRepository,
                customerMapper,
                deviceMapper
        );
    }

    @Test
    void findCustomer_NotFound() {
        // given
        Long id = 1L;
        given(customerRepository.findById(id))
                .willReturn(Optional.empty());

        // when
        Throwable actual = catchThrowable(() -> underTest.findCustomer(id));

        // then
        then(actual).isInstanceOf(NotFoundException.class);

    }

    @Test
    void findCustomer_Found() {
        // given
        Long customerId = 1L;
        Customer customer = new Customer().setId(customerId);
        CustomerDto customerDto = new CustomerDto().setId(1L);
        Device device = new Device().setCustomer(customer);
        List<Device> devices = Collections.singletonList(device);
        DeviceDto deviceDto = new DeviceDto().setCustomer(customerId);
        given(customerRepository.findById(customerId))
                .willReturn(Optional.of(customer));
        given(customerMapper.mapToDto(customer))
                .willReturn(customerDto);
        given(deviceRepository.findByCustomerId(customerId))
                .willReturn(devices);
        given(deviceMapper.mapToDto(device))
                .willReturn(deviceDto);
        CustomerWithDevicesDto expected = new CustomerWithDevicesDto()
                .setCustomer(customerDto)
                .setDevices(Collections.singletonList(deviceDto));

        // when
        CustomerWithDevicesDto actual = underTest.findCustomer(customerId);

        // then
        then(actual).isEqualTo(expected);

    }

    @Test
    void updateAddress_NotUpdated() {
        // given
        Long id = 1L;
        CustomerAddressDto dto = new CustomerAddressDto().setNewAddress("New Address!");
        given(customerRepository.findById(id))
                .willReturn(Optional.empty());


        // when
        Throwable actual = catchThrowable(() -> underTest.updateAddress(id, dto));

        // then
        then(actual).isInstanceOf(NotFoundException.class);

    }

    @Test
    void updateAddress_updated() {
        // given
        Long id = 1L;
        String expected = "New Address!";
        CustomerAddressDto dto = new CustomerAddressDto().setNewAddress(expected);
        Customer customer = new Customer().setId(id).setAddress("Old Address");
        given(customerRepository.findById(id))
                .willReturn(Optional.of(customer));

        // when
        underTest.updateAddress(id, dto);

        // then
        then(customer.getAddress()).isEqualTo(expected);
    }


    @Test
    void saveCustomer_Saved() {
        // given
        Long expected = 1L;
        CustomerDto dto = new CustomerDto().setName("Andrea");
        Customer customer = new Customer().setId(expected);
        given(customerMapper.mapToEntity(dto))
                .willReturn(customer);
        given(customerRepository.save(customer))
                .willReturn(customer);

        // when
        Long actual = underTest.saveCustomer(dto);

        // then
        then(actual).isEqualTo(expected);
    }

    @Test
    void deleteCustomer_NotDeleted() {
        // given
        Long id = 1L;
        given(customerRepository.findById(id))
                .willReturn(Optional.empty());

        // when
        Throwable actual = catchThrowable(() -> underTest.deleteCustomer(id));

        // then
        then(actual).isInstanceOf(NotFoundException.class);

    }

    @Test
    void deleteCustomer_Deleted() {
        // given
        Long id = 1L;
        Customer customer = new Customer().setId(id);
        given(customerRepository.findById(id))
                .willReturn(Optional.of(customer));

        // when
        underTest.deleteCustomer(id);

        // then
        BDDMockito.then(customerRepository).should().delete(customer);

    }
}