package org.elkendev.testutil.rest;

import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class TestRestTemplate extends RestTemplate {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestRestTemplate.class);

    @Override
    public <T> T getForObject(String url, Class<T> responseType, Object... urlVariables) throws RestClientException {
        return perform(() -> super.getForObject(url, responseType, urlVariables));
    }

    private <T> T perform(Supplier<T> supplier) {
        T obj = null;
        try {
            obj = supplier.get();
        } catch (HttpClientErrorException e) {
            //not found
            LOGGER.error("", e);
        }
        return obj;
    }
}