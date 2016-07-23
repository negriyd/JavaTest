package org.javatest.multithreading;

import java.util.concurrent.Callable;

/**
 * Created by negriyd on 23.07.2016.
 */
public class CallableTask implements Callable<Long> {

    private final Runnable task;

    public CallableTask(Runnable task) {
        this.task = task;
    }
    public Long call() throws Exception {
        long startTime = System.currentTimeMillis();
        task.run();
        return System.currentTimeMillis() - startTime;
    }
}
