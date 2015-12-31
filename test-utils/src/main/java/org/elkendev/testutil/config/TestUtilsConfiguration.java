package org.elkendev.testutil.config;

import org.elkendev.testutil.rest.OnClientError;
import org.elkendev.testutil.rest.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.client.RestOperations;

@EnableAspectJAutoProxy
@Configuration
public class TestUtilsConfiguration {

    @Bean
    public RestOperations testRestTemplate() {
        return new TestRestTemplate();
    }

    @Bean
    public OnClientError onClientError() {
        return new OnClientError();
    }

}
