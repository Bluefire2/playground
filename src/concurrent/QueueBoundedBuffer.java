package concurrent;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class QueueBoundedBuffer<T> implements BoundedBuffer<T> {
    private final Queue<T> queue;
    private final int capacity;
    private int waiting;

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition spaceExists = lock.newCondition();
    private final Condition nonEmpty = lock.newCondition();

    public QueueBoundedBuffer(int capacity) {
        this.queue = new LinkedList<>();
        this.capacity = capacity;
        this.waiting = 0;
    }

    @Override
    public int size() {
        return queue.size();
    }

    @Override
    public int capacity() {
        return capacity;
    }

    @Override
    public void add(T element) throws InterruptedException {
        lock.lock();
        if (queue.size() == capacity && waiting == 0) {
            spaceExists.await();
        }

        queue.add(element);
        nonEmpty.signal();
        lock.unlock();
    }

    @Override
    public T remove() throws InterruptedException {
        lock.lock();
        waiting++;
        while (queue.isEmpty()) {
            nonEmpty.await();
        }
        waiting--;

        T elem = queue.remove();
        spaceExists.signal();
        lock.unlock();

        return elem;
    }
}
