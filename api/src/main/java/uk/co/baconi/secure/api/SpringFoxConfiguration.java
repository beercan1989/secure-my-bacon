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

package uk.co.baconi.secure.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import com.fasterxml.classmate.TypeResolver;
import uk.co.baconi.secure.api.exceptions.ExceptionResponse;
import uk.co.baconi.secure.api.exceptions.ValidationResponse;

import static com.google.common.collect.Sets.newHashSet;

import static springfox.documentation.swagger.web.UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS;

@Configuration
@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class)
public class SpringFoxConfiguration {

    @Autowired
    private ApiVersion apiVersion;

    @Bean
    public Docket apiDocumentation(final TypeResolver typeResolver) {

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .build()
                .genericModelSubstitutes(ResponseEntity.class)
                .useDefaultResponseMessages(false)
                .protocols(newHashSet("http", "https"))
                .additionalModels(
                    typeResolver.resolve(ExceptionResponse.class),
                    typeResolver.resolve(ValidationResponse.class)
                );
    }

    @Bean
    public UiConfiguration uiConfig() {
        return new UiConfiguration(
                null,                   // url
                "list",                 // docExpansion           => none | list
                "alpha",                // apiSorter              => alpha
                "schema",               // defaultModelRendering  => schema
                DEFAULT_SUBMIT_METHODS, // supportedSubmitMethods => { "get", "post", "put", "delete", "patch" }
                false,                  // enableJsonEditor       => true | false
                true);                  // showRequestHeaders     => true | false
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .version(apiVersion.getVersion())
                .title("Secure My Bacon")
                .description("A collection of API's to provide all your Identity needs.")
                .license("Apache 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0")
                .termsOfServiceUrl("https://tosdr.org/")
                .contact(new Contact("Secure My Bacon", "https://github.com/beercan1989/secure-my-bacon", null))
                .build();
    }
}
