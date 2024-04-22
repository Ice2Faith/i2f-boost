package i2f.container;

import i2f.container.reference.Reference;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Ice2Faith
 * @date 2024/4/12 11:21
 * @desc
 * 一个线程安全的环形队列
 */
public class RingQueue<E> {
    protected ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    protected Lock readLock = readWriteLock.readLock();
    protected Lock writeLock = readWriteLock.writeLock();
    ;
    protected Condition condEnqueue = writeLock.newCondition();
    protected Condition condDequeue = writeLock.newCondition();

    protected volatile Reference<E>[] arr;
    protected volatile int curr;
    protected volatile int end;

    public RingQueue(int size) {
        arr = new Reference[size + 1];
        curr = arr.length - 1;
        end = 0;
    }


    public boolean isEmpty() {
        readLock.lock();
        try {
            return (curr + 1) % arr.length == end;
        } finally {
            readLock.unlock();
        }

    }

    public boolean isFull() {
        readLock.lock();
        try {
            return end == curr;
        } finally {
            readLock.unlock();
        }
    }

    public int size() {
        readLock.lock();
        try {
            return ((end - 1 + arr.length) - curr) % arr.length;
        } finally {
            readLock.unlock();
        }
    }

    public void clear() {
        writeLock.lock();
        try {
            curr = arr.length - 1;
            end = 0;
            condEnqueue.signal();
        } finally {
            writeLock.unlock();
        }
    }

    protected void innerEnqueue(Reference<E> val) {
        arr[end] = val;
        end = (end + 1) % arr.length;
    }

    protected Reference<E> innerDequeue() {
        curr = (curr + 1) % arr.length;
        return arr[curr];
    }

    public E get(int i) {
        readLock.lock();
        try {
            return arr[(curr + 1 + i) % arr.length].get();
        } finally {
            readLock.unlock();
        }
    }

    public void set(int i, E val) {
        writeLock.lock();
        try {
            arr[(curr + 1 + i) % arr.length] = Reference.of(val);
        } finally {
            writeLock.unlock();
        }
    }

    public void enqueue(E val) {
        writeLock.lock();
        try {
            if (isFull()) {
                condEnqueue.await();
            }
            innerEnqueue(Reference.of(val));
            condDequeue.signal();
        } catch (InterruptedException e) {
            throw new IllegalStateException("queue has full.");
        } finally {
            writeLock.unlock();
        }
    }

    public <C extends Collection<E>> void enqueueAll(C col) {
        if (col == null) {
            return;
        }
        for (E item : col) {
            enqueue(item);
        }
    }

    public E dequeue() {
        writeLock.lock();
        try {
            if (isEmpty()) {
                condEnqueue.signal();
                condDequeue.await();
            }
            Reference<E> ret = innerDequeue();
            condEnqueue.signal();
            return ret.get();
        } catch (InterruptedException e) {
            throw new IllegalStateException("queue has empty.");
        } finally {
            writeLock.unlock();
        }
    }


    public Reference<E> dequeueIf() {
        writeLock.lock();
        try {
            if (isEmpty()) {
                condEnqueue.signal();
                return Reference.nop();
            }
            Reference<E> ret = innerDequeue();
            condEnqueue.signal();
            return ret;
        } finally {
            writeLock.unlock();
        }
    }

    public List<E> dequeueAllIf() {
        return dequeueAllIf(new LinkedList<>());
    }

    public List<E> dequeueAllIf(int maxCount) {
        return dequeueAllIf(new LinkedList<>(), maxCount);
    }

    public <C extends Collection<E>> C dequeueAllIf(C ret) {
        return dequeueAllIf(ret, -1);
    }

    public <C extends Collection<E>> C dequeueAllIf(C ret, int maxCount) {
        writeLock.lock();
        try {
            boolean isAction = false;
            int recvCount = 0;
            while (!isEmpty()) {
                Reference<E> elem = innerDequeue();
                ret.add(elem.get());
                isAction = true;
                recvCount++;
                if (recvCount == maxCount) {
                    break;
                }
            }
            if (isAction || isEmpty()) {
                condEnqueue.signal();
            }
            return ret;
        } finally {
            writeLock.unlock();
        }
    }

    public Reference<E> head() {
        readLock.lock();
        try {
            if (isEmpty()) {
                return Reference.nop();
            }
            int idx = (curr + 1) % arr.length;
            return arr[idx];
        } finally {
            readLock.unlock();
        }
    }

    public Reference<E> tail() {
        readLock.lock();
        try {
            if (isEmpty()) {
                return Reference.nop();
            }
            int idx = (end - 1 + arr.length) % arr.length;
            return arr[idx];
        } finally {
            readLock.unlock();
        }
    }

}
