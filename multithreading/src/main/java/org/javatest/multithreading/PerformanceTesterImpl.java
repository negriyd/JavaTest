package org.javatest.multithreading;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Created by negriyd on 23.07.2016.
 */
public class PerformanceTesterImpl implements PerformanceTester {

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int calculationCount  = Integer.parseInt(args[1]);
        int threadPoolSize  = Integer.parseInt(args[2]);

        Runnable task = new Runnable() {
            @Override
            public void run() {
                FibCalc fibCalc = new FibCalcImpl();
                fibCalc.fib(n);
            }
        };

        PerformanceTester performanceTester = new PerformanceTesterImpl();
        try {
            System.out.println(performanceTester.runPerformanceTest(task, calculationCount, threadPoolSize));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public PerformanceTestResult runPerformanceTest(Runnable task, int executionCount, int threadPoolSize)
            throws InterruptedException
    {
        ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize);
        Collection<Callable<Long>> tasks = new ArrayList();

        for (int i = 0; i < executionCount; i++)
            tasks.add(new CallableTask(task));

        long startTime = System.currentTimeMillis();
        List<Future<Long>> results = executor.invokeAll(tasks);

        long totalTime = 0;
        long minTime = Long.MAX_VALUE;
        long maxTime = Long.MIN_VALUE;

        boolean inProgress = true;
        while (inProgress) {
            // If all streams done
            if (results.stream().filter(f -> f.isDone()).collect(Collectors.toList()).size() == executionCount) {
                totalTime = System.currentTimeMillis() - startTime;
                inProgress = false;
                for (Future<Long> result : results) {
                    try {
                        long taskExecutionTime = result.get();
                        minTime = taskExecutionTime < minTime ? taskExecutionTime : minTime;
                        maxTime = taskExecutionTime > maxTime ? taskExecutionTime : maxTime;
                    } catch (ExecutionException e) {
                        throw new RuntimeException(e);
                    }

                }
            }

        }
        executor.shutdown();


        return new PerformanceTestResult(totalTime, minTime, maxTime);
    }
}
