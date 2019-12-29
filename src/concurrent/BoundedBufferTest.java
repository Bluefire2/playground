package concurrent;

import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

abstract class BoundedBufferTest {
    abstract <T> BoundedBuffer<T> newInstance(int capacity);

    @Test
    public void canInsertUpToCapacity() throws InterruptedException {
        BoundedBuffer<Integer> buffer = newInstance(5);
        for (int i = 0; i < 5; i++) {
            buffer.add(i);
        }
    }

    @Test
    public void cannotInsertBeyondCapacity() throws InterruptedException {
        BoundedBuffer<Integer> buffer = newInstance(10);
        // should work fine here
        for (int i = 0; i < 10; i++) {
            buffer.add(i);
        }

        // now, inserting should block:
        Thread runner = new Thread(() -> {
            try {
                buffer.add(123);
            } catch (InterruptedException ignored) {

            }
        });
        runner.start();

        Thread.sleep(2000);
        assertEquals(10, buffer.size());
    }

    @Test
    public void zeroCapacityBufferWorks() throws ExecutionException, InterruptedException {
        BoundedBuffer<Integer> buffer = newInstance(0);
        ExecutorService executor = Executors.newFixedThreadPool(10);

        Runnable adder = () -> {
            try {
                buffer.add(777);
            } catch (InterruptedException ignored) {

            }
        };
        Callable<Integer> remover = buffer::remove;

        Future<Integer> removed = executor.submit(remover);
        executor.submit(adder);

        assertEquals(777, removed.get());
    }

    @Test
    public void doesNotDeadlockUnderHeavyUse() {
        BoundedBuffer<Integer> buffer = newInstance(0);
        ExecutorService executor = Executors.newFixedThreadPool(10);

        Runnable adder = () -> {
            try {
                for (int i = 0; i < 1000000; i++) {
                    buffer.add(i);
                }
            } catch (InterruptedException ignored) {

            }
        };

        Runnable remover = () -> {
            try {
                for (int i = 0; i < 1000000; i++) {
                    buffer.remove();
                }
            } catch (InterruptedException ignored) {

            }
        };

        executor.submit(adder);
        executor.submit(remover);

        assertEquals(0, buffer.size());
    }
}