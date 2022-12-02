package unit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import util.TelepassSpringTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TelepassSpringTest
class ApplicationContextTest {

    @Test
    void contextLoads() {
        // if the application context has some errors then this test fails
        assertTrue(true);
    }


}
