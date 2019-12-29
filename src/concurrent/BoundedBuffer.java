package concurrent;

public interface BoundedBuffer<T> {
    /**
     * Get the number of elements in the buffer.
     */
    int size();

    /**
     * Get the maximum size of the buffer.
     */
    int capacity();

    /**
     * Insert an element into the buffer. If the buffer is at maximum capacity, block the current thread until space
     * becomes available.
     *
     * @param element The element to insert.
     */
    void add(T element) throws InterruptedException;

    /**
     * Remove an element from the buffer (FIFO). If the buffer is empty, block the current thread until an element is
     * added.
     *
     * @return The least-recently inserted element.
     */
    T remove() throws InterruptedException;
}
