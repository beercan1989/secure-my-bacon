package uk.co.baconi.secure.api;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import static org.assertj.core.api.Assertions.assertThat;

@Ignore("Broken due to Java 11 upgrade.") // TODO - Fix PowerMockito
@RunWith(PowerMockRunner.class)
@PrepareForTest(SpringApplication.class)
public class ApiApplicationTest {

    @Test
    public void shouldRunSpringApplicationViaMain() {

        // Setup static mock
        PowerMockito.mockStatic(SpringApplication.class);

        ApiApplication.main("Test Argument");

        // Verify static is called
        PowerMockito.verifyStatic(Mockito.times(1));
        SpringApplication.run(Mockito.eq(ApiApplication.class), Mockito.eq("Test Argument"));
    }

    @Test
    public void shouldCreateMethodValidationPostProcessorBean() {

        final Object underTest = new ApiApplication().methodValidationPostProcessor();

        assertThat(underTest).isInstanceOf(MethodValidationPostProcessor.class);
    }

}
