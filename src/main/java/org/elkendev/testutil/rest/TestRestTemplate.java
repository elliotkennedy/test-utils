package org.elkendev.testutil.rest;

import java.util.function.Supplier;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class TestRestTemplate extends RestTemplate {

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
        }
        return obj;
    }
}