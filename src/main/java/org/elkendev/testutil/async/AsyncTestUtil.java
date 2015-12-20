package org.elkendev.testutil.async;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.truth.Truth.assertWithMessage;

public class AsyncTestUtil {

    private static final Logger LOG = LoggerFactory.getLogger(AsyncTestUtil.class);

    private static final long WAIT_FOR_TIMEOUT = 30000L;

    private static final long DEFAULT_WAIT = 250L;

    /**
     * Sit in a slow loop until a predicate is matched
     *
     * @param producer
     * @param predicate
     */
    public static <T> void waitUntil(Supplier<T> producer, Predicate<T> predicate) {
        waitUntil(producer, predicate, DEFAULT_WAIT, WAIT_FOR_TIMEOUT);
    }

    /**
     * Sit in a slow loop until a predicate is matched
     *
     * @param producer
     * @param predicate
     */
    public static <T> void waitUntil(Supplier<T> producer, Predicate<T> predicate, long sleepTime, long maxWaitTime) {
        long start = System.currentTimeMillis();
        T value = producer.get();
        while (!predicate.test(value)) {
            try {
                Thread.sleep(sleepTime);
                assertWithMessage("Retry timeout exceeded").that(System.currentTimeMillis() - start).isLessThan(maxWaitTime);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            value = producer.get();
        }
    }

    /**
     * Sit in a slow loop until an assert statement is matched
     *
     * @param producer
     * @param matchers
     */
    public static <T> void waitUntilMatches(Supplier<T> producer, Consumer<T> matchers, long sleepTime, long maxWaitTime) {
        long time = 0;
        AssertionError error = null;
        while (time < maxWaitTime) {
            try {
                matchers.accept(producer.get());
                return;
            } catch (AssertionError e) {
                error = e;
                LOG.error("", e);
                try {
                    Thread.sleep(sleepTime);
                    time = time + sleepTime;
                } catch (InterruptedException ie) {
                    throw new RuntimeException(ie);
                }
            }
        }
        throw error;
    }

    /**
     * Sit in a slow loop until an assert statement is matched
     *
     * @param producer
     * @param matchers
     */
    public static <T> void waitUntilMatches(Supplier<T> producer, Consumer<T> matchers) {
        waitUntilMatches(producer, matchers, DEFAULT_WAIT, WAIT_FOR_TIMEOUT);
    }
}