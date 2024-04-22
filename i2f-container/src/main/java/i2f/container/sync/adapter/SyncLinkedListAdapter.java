package i2f.container.sync.adapter;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

/**
 * @author Ice2Faith
 * @date 2024/4/18 14:47
 * @desc
 */
public class SyncLinkedListAdapter<E> extends LinkedList<E> {
    private LinkedList<E> list;
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    public SyncLinkedListAdapter(LinkedList<E> list) {
        this.list = list;
    }

    public SyncLinkedListAdapter(LinkedList<E> list, ReadWriteLock lock) {
        this.list = list;
        this.lock = lock;
    }

    @Override
    public E getFirst() {
        lock.readLock().lock();
        try {
            return list.getFirst();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public E getLast() {
        lock.readLock().lock();
        try {
            return list.getLast();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public E removeFirst() {
        lock.writeLock().lock();
        try {
            return list.removeFirst();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public E removeLast() {
        lock.writeLock().lock();
        try {
            return list.removeLast();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void addFirst(E e) {
        lock.writeLock().lock();
        try {
            list.addFirst(e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void addLast(E e) {
        lock.writeLock().lock();
        try {
            list.addLast(e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean contains(Object o) {
        lock.readLock().lock();
        try {
            return list.contains(o);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public int size() {
        lock.readLock().lock();
        try {
            return list.size();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean add(E e) {
        lock.writeLock().lock();
        try {
            return list.add(e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean remove(Object o) {
        lock.writeLock().lock();
        try {
            return list.remove(o);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        lock.writeLock().lock();
        try {
            return list.addAll(c);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        lock.writeLock().lock();
        try {
            return list.addAll(index, c);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void clear() {
        lock.writeLock().lock();
        try {
            list.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public E get(int index) {
        lock.readLock().lock();
        try {
            return list.get(index);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public E set(int index, E element) {
        lock.writeLock().lock();
        try {
            return list.set(index, element);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void add(int index, E element) {
        lock.writeLock().lock();
        try {
            list.add(index, element);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public E remove(int index) {
        lock.writeLock().lock();
        try {
            return list.remove(index);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public int indexOf(Object o) {
        lock.readLock().lock();
        try {
            return list.indexOf(o);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public int lastIndexOf(Object o) {
        lock.readLock().lock();
        try {
            return list.lastIndexOf(o);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public E peek() {
        lock.readLock().lock();
        try {
            return list.peek();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public E element() {
        lock.readLock().lock();
        try {
            return list.element();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public E poll() {
        lock.writeLock().lock();
        try {
            return list.poll();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public E remove() {
        lock.writeLock().lock();
        try {
            return list.remove();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean offer(E e) {
        lock.writeLock().lock();
        try {
            return list.offer(e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean offerFirst(E e) {
        lock.writeLock().lock();
        try {
            return list.offerFirst(e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean offerLast(E e) {
        lock.writeLock().lock();
        try {
            return list.offerLast(e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public E peekFirst() {
        lock.readLock().lock();
        try {
            return list.peekFirst();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public E peekLast() {
        lock.readLock().lock();
        try {
            return list.peekLast();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public E pollFirst() {
        lock.writeLock().lock();
        try {
            return list.pollFirst();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public E pollLast() {
        lock.writeLock().lock();
        try {
            return list.pollLast();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void push(E e) {
        lock.writeLock().lock();
        try {
            list.push(e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public E pop() {
        lock.writeLock().lock();
        try {
            return list.pop();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        lock.writeLock().lock();
        try {
            return list.removeFirstOccurrence(o);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        lock.writeLock().lock();
        try {
            return list.removeLastOccurrence(o);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        lock.readLock().lock();
        try {
            return new SyncListIteratorAdapter<>(list.listIterator(index), lock);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Iterator<E> descendingIterator() {
        lock.readLock().lock();
        try {
            return new SyncIteratorAdapter<>(list.descendingIterator(), lock);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Object clone() {
        lock.readLock().lock();
        try {
            return list.clone();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Object[] toArray() {
        lock.readLock().lock();
        try {
            return list.toArray();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public <T> T[] toArray(T[] a) {
        lock.readLock().lock();
        try {
            return list.toArray(a);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Spliterator<E> spliterator() {
        lock.readLock().lock();
        try {
            return new SyncSpliteratorAdapter<>(list.spliterator(), lock);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Iterator<E> iterator() {
        lock.readLock().lock();
        try {
            return new SyncIteratorAdapter<>(list.iterator(), lock);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public ListIterator<E> listIterator() {
        lock.readLock().lock();
        try {
            return new SyncListIteratorAdapter<>(list.listIterator(), lock);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        lock.readLock().lock();
        try {
            return list.subList(fromIndex, toIndex);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean equals(Object o) {
        lock.readLock().lock();
        try {
            return list.equals(o);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public int hashCode() {
        lock.readLock().lock();
        try {
            return list.hashCode();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean isEmpty() {
        lock.readLock().lock();
        try {
            return list.isEmpty();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        lock.readLock().lock();
        try {
            return list.containsAll(c);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        lock.writeLock().lock();
        try {
            return list.removeAll(c);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        lock.writeLock().lock();
        try {
            return list.retainAll(c);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public String toString() {
        lock.readLock().lock();
        try {
            return list.toString();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void replaceAll(UnaryOperator<E> operator) {
        lock.writeLock().lock();
        try {
            list.replaceAll(operator);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void sort(Comparator<? super E> c) {
        lock.writeLock().lock();
        try {
            list.sort(c);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        lock.writeLock().lock();
        try {
            return list.removeIf(filter);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Stream<E> stream() {
        lock.readLock().lock();
        try {
            return list.stream();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Stream<E> parallelStream() {
        lock.readLock().lock();
        try {
            return list.parallelStream();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        lock.readLock().lock();
        try {
            list.forEach(action);
        } finally {
            lock.readLock().unlock();
        }
    }

}
