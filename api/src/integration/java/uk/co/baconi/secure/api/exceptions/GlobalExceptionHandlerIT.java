package uk.co.baconi.secure.api.exceptions;

import org.junit.Test;
import uk.co.baconi.secure.api.integrations.IntegratedApiEndpoint;

import static org.hamcrest.Matchers.*;

public class GlobalExceptionHandlerIT extends IntegratedApiEndpoint {

    @Test
    public void onHandlingBasicExceptions() {

        withNoAuthentication().
                get("/fake/throw-error").

                then().
                assertThat().

                statusCode(is(equalTo(500))).
                and().
                body("uuid", isA(String.class)).
                body("name", isA(String.class)).
                body("name", is(equalTo("uk.co.baconi.secure.api.integrations.IntegrationApiApplication$IntegrationTestController$FakeException")));

    }

}
