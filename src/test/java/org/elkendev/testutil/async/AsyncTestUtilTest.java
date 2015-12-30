package org.elkendev.testutil.async;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;
import static org.elkendev.testutil.async.AsyncTestUtil.waitUntilMatches;

public class AsyncTestUtilTest {

    private ExecutorService executor;

    @Before
    public void setup() {
        executor = Executors.newSingleThreadExecutor();
    }

    @After
    public void tearDown() {
        executor.shutdownNow();
    }

    @Test(expected = AssertionError.class)
    public void testWaitUntilMatches_failTestWhenAssertionNeverTrue() {
        waitUntilMatches(() -> true, (result) -> assertThat(result).isFalse(), 0L, 0L);
    }

    @Test
    public void testWaitUntilMatches_passTestWhenConditionIsMetWithinTime() {
        Future<Void> future = executor.submit(new Task(100L));
        waitUntilMatches(future::isDone, ((conditionMet) -> assertWithMessage("Task not complete").that(conditionMet).isTrue()), 10L, 500L);
    }

    @Test(expected = AssertionError.class)
    public void testWaitUntilMatches_whenConditionIsNotMetInTime_failAfterTimeout() {
        Future<Void> future = executor.submit(new Task(1000L));
        waitUntilMatches(future::isDone, ((conditionMet) -> assertWithMessage("Task not complete").that(conditionMet).isTrue()), 10L, 100L);
    }

    class Task implements Callable<Void> {

        private final long taskTime;

        public Task(final long taskTime) {
            this.taskTime = taskTime;
        }

        @Override
        public Void call() {
            try {
                Thread.sleep(taskTime);
                return null;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
