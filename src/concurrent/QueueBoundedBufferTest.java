package concurrent;

public class QueueBoundedBufferTest extends BoundedBufferTest {
    @Override
    <T> BoundedBuffer<T> newInstance(int capacity) {
        return new QueueBoundedBuffer<>(capacity);
    }
}
