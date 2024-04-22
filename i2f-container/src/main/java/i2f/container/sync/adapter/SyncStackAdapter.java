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
 * @date 2024/4/18 17:09
 * @desc
 */
public class SyncStackAdapter<E> extends Stack<E> {
    private Stack<E> stack;
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    public SyncStackAdapter(Stack<E> stack) {
        this.stack = stack;
    }

    public SyncStackAdapter(Stack<E> stack, ReadWriteLock lock) {
        this.stack = stack;
        this.lock = lock;
    }

    @Override
    public E push(E item) {
        lock.writeLock().lock();
        try {
            return stack.push(item);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public E pop() {
        lock.writeLock().lock();
        try {
            return stack.pop();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public E peek() {
        lock.readLock().lock();
        try {
            return stack.peek();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean empty() {
        lock.readLock().lock();
        try {
            return stack.empty();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public int search(Object o) {
        lock.readLock().lock();
        try {
            return stack.search(o);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void copyInto(Object[] anArray) {
        lock.writeLock().lock();
        try {
            stack.copyInto(anArray);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void trimToSize() {
        lock.writeLock().lock();
        try {
            stack.trimToSize();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void ensureCapacity(int minCapacity) {
        lock.writeLock().lock();
        try {
            stack.ensureCapacity(minCapacity);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void setSize(int newSize) {
        lock.writeLock().lock();
        try {
            stack.setSize(newSize);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public int capacity() {
        lock.readLock().lock();
        try {
            return stack.capacity();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public int size() {
        lock.readLock().lock();
        try {
            return stack.size();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean isEmpty() {
        lock.readLock().lock();
        try {
            return stack.isEmpty();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Enumeration<E> elements() {
        lock.readLock().lock();
        try {
            return new SyncEnumerationAdapter<>(stack.elements(), lock);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean contains(Object o) {
        lock.readLock().lock();
        try {
            return stack.contains(o);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public int indexOf(Object o) {
        lock.readLock().lock();
        try {
            return stack.indexOf(o);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public int indexOf(Object o, int index) {
        lock.readLock().lock();
        try {
            return stack.indexOf(o, index);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public int lastIndexOf(Object o) {
        lock.readLock().lock();
        try {
            return stack.lastIndexOf(o);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public int lastIndexOf(Object o, int index) {
        lock.readLock().lock();
        try {
            return stack.lastIndexOf(o, index);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public E elementAt(int index) {
        lock.readLock().lock();
        try {
            return stack.elementAt(index);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public E firstElement() {
        lock.readLock().lock();
        try {
            return stack.firstElement();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public E lastElement() {
        lock.readLock().lock();
        try {
            return stack.lastElement();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void setElementAt(E obj, int index) {
        lock.writeLock().lock();
        try {
            stack.setElementAt(obj, index);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void removeElementAt(int index) {
        lock.writeLock().lock();
        try {
            stack.removeElementAt(index);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void insertElementAt(E obj, int index) {
        lock.writeLock().lock();
        try {
            stack.insertElementAt(obj, index);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void addElement(E obj) {
        lock.writeLock().lock();
        try {
            stack.addElement(obj);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean removeElement(Object obj) {
        lock.writeLock().lock();
        try {
            return stack.removeElement(obj);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void removeAllElements() {
        lock.writeLock().lock();
        try {
            stack.removeAllElements();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Object clone() {
        lock.readLock().lock();
        try {
            return stack.clone();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Object[] toArray() {
        lock.readLock().lock();
        try {
            return stack.toArray();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public <T> T[] toArray(T[] a) {
        lock.readLock().lock();
        try {
            return stack.toArray(a);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public E get(int index) {
        lock.readLock().lock();
        try {
            return stack.get(index);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public E set(int index, E element) {
        lock.writeLock().lock();
        try {
            return stack.set(index, element);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean add(E e) {
        lock.writeLock().lock();
        try {
            return stack.add(e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean remove(Object o) {
        lock.writeLock().lock();
        try {
            return stack.remove(o);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void add(int index, E element) {
        lock.writeLock().lock();
        try {
            stack.add(index, element);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public E remove(int index) {
        lock.writeLock().lock();
        try {
            return stack.remove(index);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void clear() {
        lock.writeLock().lock();
        try {
            stack.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        lock.readLock().lock();
        try {
            return stack.containsAll(c);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        lock.writeLock().lock();
        try {
            return stack.addAll(c);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        lock.writeLock().lock();
        try {
            return stack.remove(c);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        lock.writeLock().lock();
        try {
            return stack.retainAll(c);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        lock.writeLock().lock();
        try {
            return stack.addAll(index, c);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean equals(Object o) {
        lock.readLock().lock();
        try {
            return stack.equals(o);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public int hashCode() {
        lock.readLock().lock();
        try {
            return stack.hashCode();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public String toString() {
        lock.readLock().lock();
        try {
            return stack.toString();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        lock.readLock().lock();
        try {
            return stack.subList(fromIndex, toIndex);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        lock.readLock().lock();
        try {
            return new SyncListIteratorAdapter<>(stack.listIterator(index), lock);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public ListIterator<E> listIterator() {
        lock.readLock().lock();
        try {
            return new SyncListIteratorAdapter<>(stack.listIterator(), lock);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Iterator<E> iterator() {
        lock.readLock().lock();
        try {
            return new SyncIteratorAdapter<>(stack.iterator(), lock);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        lock.readLock().lock();
        try {
            stack.forEach(action);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        lock.writeLock().lock();
        try {
            return stack.removeIf(filter);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void replaceAll(UnaryOperator<E> operator) {
        lock.writeLock().lock();
        try {
            stack.replaceAll(operator);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void sort(Comparator<? super E> c) {
        lock.writeLock().lock();
        try {
            stack.sort(c);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Spliterator<E> spliterator() {
        lock.readLock().lock();
        try {
            return new SyncSpliteratorAdapter<>(stack.spliterator(), lock);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Stream<E> stream() {
        lock.readLock().lock();
        try {
            return stack.stream();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Stream<E> parallelStream() {
        lock.readLock().lock();
        try {
            return stack.parallelStream();
        } finally {
            lock.readLock().unlock();
        }
    }

}
