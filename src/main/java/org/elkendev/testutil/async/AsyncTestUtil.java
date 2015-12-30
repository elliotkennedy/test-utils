package org.elkendev.testutil.async;

import java.util.function.Consumer;
import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AsyncTestUtil {

    private static final Logger LOG = LoggerFactory.getLogger(AsyncTestUtil.class);

    private static final long WAIT_FOR_TIMEOUT = 30000L;
    private static final long DEFAULT_WAIT = 250L;

    /**
     * Sit in a slow loop until an assert statement is matched
     *
     * @param producer
     * @param matchers
     */
    public static <T> void waitUntilMatches(Supplier<T> producer, Consumer<T> matchers, long sleepTime, long maxWaitTime) {
        long time = 0L;
        AssertionError error = null;
        while (time <= maxWaitTime) {
            try {
                matchers.accept(producer.get());
                return;
            } catch (AssertionError e) {
                error = e;
                LOG.error("", e);
                try {
                    Thread.sleep(sleepTime);
                    time = time + (sleepTime != 0L ? sleepTime : 1L);
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
