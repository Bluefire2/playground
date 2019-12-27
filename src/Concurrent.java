import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Concurrent {
    private static final ExecutorService executor = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {
        Runnable task1 = createCounterThread(1);
        Runnable task2 = createCounterThread(2);
        Runnable task3 = createCounterThread(3);

        executor.execute(task1);
        executor.execute(task2);
        executor.execute(task3);

        executor.shutdown();
    }

    private static Runnable createCounterThread(int id) {
        return () -> {
            for (int i = 1; i <= 10; i++) {
                System.out.println("Thread " + id + " says: " + i);
            }
        };
    }
}
