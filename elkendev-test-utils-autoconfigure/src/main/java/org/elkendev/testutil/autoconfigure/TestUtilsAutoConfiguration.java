package org.elkendev.testutil.autoconfigure;

import org.elkendev.testutil.rest.OnClientError;
import org.elkendev.testutil.rest.TestRestTemplate;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestOperations;

@Configuration
@AutoConfigureAfter(AopAutoConfiguration.class)
public class TestUtilsAutoConfiguration {

    @Bean
    public RestOperations testRestTemplate() {
        return new TestRestTemplate();
    }

    @Bean
    public OnClientError onClientError() {
        return new OnClientError();
    }

}
