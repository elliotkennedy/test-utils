package org.elkendev.testutil.rest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

@Aspect
public class OnClientError {

    private static final Logger LOGGER = LoggerFactory.getLogger(OnClientError.class);

    @Pointcut("target(org.elkendev.testutil.rest.TestRestTemplate)")
    public void restOperation() {}

    @Around("restOperation()")
    public void swallow404(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        try {
            proceedingJoinPoint.proceed();
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() != HttpStatus.NOT_FOUND) {
                throw e;
            }
            LOGGER.error("", e);
        }
    }
}
