package org.elkendev.testutil.rest;

import org.elkendev.testutil.config.TestUtilsConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import static com.google.common.truth.Truth.assertThat;
import static net.jadler.Jadler.closeJadler;
import static net.jadler.Jadler.initJadler;
import static net.jadler.Jadler.onRequest;
import static net.jadler.Jadler.port;

@ContextConfiguration(classes = { TestUtilsConfiguration.class, TestRestTemplateTest.TestConfig.class })
@RunWith(SpringJUnit4ClassRunner.class)
public class TestRestTemplateTest {

    @Autowired
    RestOperations testRestTemplate;
    @Autowired
    RestOperations normalRestTemplate;

    @Before
    public void setup() {
        initJadler();
    }

    @After
    public void tearDown() {
        closeJadler();
    }

    @Test
    public void test404ReturnsNull() {
        onRequest()
                .havingMethodEqualTo("GET")
                .havingPathEqualTo("/accounts/1")
                .respond()
                .withStatus(404)
                .withContentType("application/json; charset=UTF-8");

        Object object = testRestTemplate.getForObject(String.format("http://localhost:%s/accounts/1", port()), Object.class);

        assertThat(object).isNull();
    }

    @Test(expected = HttpClientErrorException.class)
    public void test4xxThrowsException() {
        onRequest()
                .havingMethodEqualTo("GET")
                .havingPathEqualTo("/accounts/1")
                .respond()
                .withStatus(400)
                .withContentType("application/json; charset=UTF-8");

        testRestTemplate.getForObject(String.format("http://localhost:%s/accounts/1", port()), Object.class);
    }

    @Test(expected = HttpClientErrorException.class)
    public void testNormalRestTemplate_stillThrowsExceptionOn404() {
        onRequest()
                .havingMethodEqualTo("GET")
                .havingPathEqualTo("/accounts/1")
                .respond()
                .withStatus(404)
                .withContentType("application/json; charset=UTF-8");

        normalRestTemplate.getForObject(String.format("http://localhost:%s/accounts/1", port()), Object.class);
    }

    @Configuration
    static class TestConfig {

        @Bean
        public RestOperations normalRestTemplate() {
            return new RestTemplate();
        }
    }
}
