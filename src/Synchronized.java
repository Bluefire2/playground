import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Synchronized {
    private static final ExecutorService executor = Executors.newFixedThreadPool(10);
    private static final Lock lock = new ReentrantLock();
    private static int result = 0;

    public static void main(String[] args) {
        Runnable incrementer = () -> {
            for (int i = 0; i < 1000000; i++) {
                lock.lock(); // thread waits until the lock becomes available, and then acquires it
                // while this thread has the lock, all other threads must wait
                result = result + 1;
                lock.unlock(); // thread releases lock
            }
        };

        Runnable decrementer = () -> {
            for (int i = 0; i < 1000000; i++) {
                lock.lock();
                result = result - 1;
                lock.unlock();
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

        // This time, access to shared data (result) is synchronized, meaning only one thread can interact with it at a time
        // So this should be zero like we would expect:
        System.out.println("The final value is: " + result);
    }
}
