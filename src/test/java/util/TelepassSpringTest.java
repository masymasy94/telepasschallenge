package util;

import com.masy.telepasschallenge.TelepassChallengeApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles
public @interface TelepassSpringTest {
    @AliasFor(attribute = "classes", annotation = SpringBootTest.class)
    Class<?>[] value() default TelepassChallengeApplication.class;

    @AliasFor(attribute = "profiles", annotation = ActiveProfiles.class)
    String[] profiles() default "test";

    @AliasFor(attribute = "webEnvironment", annotation = SpringBootTest.class)
    SpringBootTest.WebEnvironment webEnvironment() default SpringBootTest.WebEnvironment.MOCK;
}
