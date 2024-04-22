package i2f.container.sync;

import i2f.container.sync.adapter.*;

import java.util.*;

/**
 * @author Ice2Faith
 * @date 2024/4/19 15:30
 * @desc
 * 提供将常用的内置集合转换为线程安全的包装
 */
public class Syncs {
    public static <E> Collection<E> collection(Collection<E> collection) {
        return new SyncCollectionAdapter<>(collection);
    }

    public static <E> Enumeration<E> enumeration(Enumeration<E> enumeration) {
        return new SyncEnumerationAdapter<>(enumeration);
    }

    public static <E> Iterator<E> iterator(Iterator<E> iterator) {
        return new SyncIteratorAdapter<>(iterator);
    }

    public static <K, V> LinkedHashMap<K, V> linkedHashMap(LinkedHashMap<K, V> map) {
        return new SyncLinkedHashMapAdapter<>(map);
    }

    public static <E> LinkedList<E> linkedList(LinkedList<E> list) {
        return new SyncLinkedListAdapter<>(list);
    }

    public static <E> List<E> list(List<E> list) {
        return new SyncListAdapter<>(list);
    }

    public static <E> ListIterator<E> listIterator(ListIterator<E> iterator) {
        return new SyncListIteratorAdapter<>(iterator);
    }

    public static <K, V> Map<K, V> map(Map<K, V> map) {
        return new SyncMapAdapter<>(map);
    }

    public static <E> Queue<E> queue(Queue<E> queue) {
        return new SyncQueueAdapter<>(queue);
    }

    public static <E> Set<E> set(Set<E> set) {
        return new SyncSetAdapter<>(set);
    }

    public static <E> Spliterator<E> spliterator(Spliterator<E> spliterator) {
        return new SyncSpliteratorAdapter<>(spliterator);
    }

    public static <E> Stack<E> stack(Stack<E> stack) {
        return new SyncStackAdapter<>(stack);
    }
}
