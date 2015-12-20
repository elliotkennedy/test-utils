package org.elkendev.testutil.rest;

import org.junit.Before;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;
import static net.jadler.Jadler.initJadler;
import static net.jadler.Jadler.onRequest;
import static net.jadler.Jadler.port;

public class TestRestTemplateTest {

    TestRestTemplate onTest;

    @Before
    public void setup() {
        onTest = new TestRestTemplate();
        initJadler();
    }

    @Test
    public void test404ReturnsNull() {
        onRequest()
                .havingMethodEqualTo("GET")
                .havingPathEqualTo("/accounts/1")
                .havingHeaderEqualTo("Accept", "application/json")
                .respond()
                .withStatus(404)
                .withContentType("application/json; charset=UTF-8");

        Object object = onTest.getForObject(String.format("http://localhost:%s/accounts/1", port()), Object.class);

        assertThat(object).isNull();
    }
}
