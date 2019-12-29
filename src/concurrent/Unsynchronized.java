package concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Unsynchronized {
    private static final ExecutorService executor = Executors.newFixedThreadPool(10);
    private static int result = 0;

    public static void main(String[] args) {
        Runnable incrementer = () -> {
            for (int i = 0; i < 1000000; i++) {
                result = result + 1; // result++ also works but this illustrates the point better
            }
        };

        Runnable decrementer = () -> {
            for (int i = 0; i < 1000000; i++) {
                result = result - 1; // ditto
            }
        };

        executor.submit(incrementer);
        executor.submit(decrementer);
        executor.shutdown();

        // wait for all threads to complete...
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {

        }

        // The final value should be zero... right?
        System.out.println("The final value is: " + result);
    }
}
