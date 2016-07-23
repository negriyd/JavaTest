package org.javatest.multithreading;

/**
 * Created by negriyd on 23.07.2016.
 */
public class FibCalcImpl implements FibCalc {
    public long fib(int n) {
        int a = 0;
        int b = 1;

        for (int i = 0; i < n; i++) {
            int temp = a;
            a = b;
            b = temp + b;
        }
        return a;
    }
}
