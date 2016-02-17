/*
 * Copyright 2016 James Bacon
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.co.baconi.secure.api.integrations;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.specification.RequestSpecification;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.validation.constraints.NotNull;
import java.io.IOException;

import static com.jayway.restassured.RestAssured.with;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(IntegrationApiApplication.class)
@WebIntegrationTest(randomPort = true)
@ActiveProfiles("integration")
public class IntegratedApiEndpoint {

    //
    // HTTP Server Values
    //
    @Value("${local.server.port}")
    @NotNull
    protected int port;

    //
    // Api Key Values
    //
    @Value("${integration.test.data.api.headerName}")
    protected String apiKeyHeader;
    @Value("${integration.test.data.api.validKey}")
    protected String apiKeyValidValue;
    @Value("${integration.test.data.api.invalidKey}")
    protected String apiKeyInvalidValue;


    protected String getBaseUrl() {
        return "http://localhost:" + port;
    }


    protected RequestSpecification withValidAuthentication() {
        return with().header(apiKeyHeader, apiKeyValidValue);
    }


    protected RequestSpecification withNoAuthentication() {
        return with();
    }


    protected RequestSpecification withInvalidAuthentication() {
        return with().header(apiKeyHeader, apiKeyInvalidValue);
    }


    protected byte[] convertToJson(final Object object) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }
}
