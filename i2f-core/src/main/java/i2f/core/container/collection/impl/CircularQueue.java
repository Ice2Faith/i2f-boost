package i2f.core.container.collection.impl;


import i2f.core.container.array.ArrayUtil;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Ice2Faith
 * @date 2023/6/28 10:50
 * @desc
 */
public class CircularQueue<T> implements Queue<T> {
    private T[] items = (T[]) new Object[0]; // 存储队列元素的数组
    private int head; // 队头指针
    private int tail; // 队尾指针
    private int size; // 队列大小
    private ReentrantLock lock = new ReentrantLock();

    public CircularQueue(int capacity) {
        this.items = (T[]) new Object[capacity]; // 初始化数组为容量大小的Object类型数组
        this.head = 0;
        this.tail = -1;
        this.size = 0;
    }

    public boolean enqueue(T item) {
        lock.lock();
        try {
            if (isFull()) { // 如果队列已满，则返回false
                return false;
            } else { // 否则将元素添加到队尾，并更新指针和大小
                this.tail = (this.tail + 1) % this.items.length;
                this.items[this.tail] = item;
                this.size++;
                return true;
            }
        } finally {
            lock.unlock();
        }
    }


    public T dequeue() {
        lock.lock();
        try {
            if (isEmpty()) { // 如果队列为空，则返回null
                return null;
            } else { // 否则将队首元素移除，并更新指针和大小
                T item = this.items[this.head];
                this.items[this.head] = null;
                this.head = (this.head + 1) % this.items.length;
                this.size--;
                return item;
            }
        } finally {
            lock.unlock();
        }
    }

    public T head() {
        lock.lock();
        try {
            if (isEmpty()) {
                return null;
            }
            return this.items[this.head];
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean isEmpty() { // 判断队列是否为空
        return this.size == 0;
    }

    public boolean isFull() { // 判断队列是否已满
        return (this.tail + 1) % this.items.length == this.head;
    }

    public int capital() {
        return this.items.length;
    }

    public int freeSize() {
        return this.items.length - this.size;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean add(T item) {
        return enqueue(item);
    }

    @Override
    public boolean offer(T item) {
        return enqueue(item);
    }

    @Override
    public T remove() {
        return dequeue();
    }

    @Override
    public T poll() {
        return dequeue();
    }

    @Override
    public T element() {
        return head();
    }

    @Override
    public T peek() {
        return head();
    }


    @Override
    public boolean contains(java.lang.Object o) {
        lock.lock();
        try {
            for (int i = head; i != tail; i = (i + 1) % items.length) {
                if (Objects.equals(items[i], o)) {
                    return true;
                }
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int idx = head;

            @Override
            public boolean hasNext() {
                return (idx + 1) % items.length != tail;
            }

            @Override
            public T next() {
                T ret = items[idx];
                idx = (idx + 1) % items.length;
                return ret;
            }
        };
    }

    @Override
    public java.lang.Object[] toArray() {
        lock.lock();
        try {
            Object[] ret = new Object[size];
            for (int i = head, j = 0; i != tail; i = (i + 1) % items.length, j++) {
                ret[j] = (Object) items[i];
            }
            return ret;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        Object[] arr = toArray();
        return (T1[]) ArrayUtil.copy(0, arr.length, a.getClass(), arr);
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("queue not support remove element");
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        lock.lock();
        try {
            Object[] arr = toArray();
            List<Object> list = Arrays.asList(arr);
            return list.containsAll(c);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        for (T item : c) {
            boolean ok = enqueue(item);
            if (!ok) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("queue not support remove element");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("queue not support remove element");
    }

    @Override
    public void clear() {
        lock.lock();
        try {
            this.head = 0;
            this.tail = -1;
            this.size = 0;
            for (int i = 0; i < items.length; i++) {
                items[i] = null;
            }
        } finally {
            lock.unlock();
        }
    }
}

