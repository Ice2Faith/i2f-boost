package i2f.stream.impl;

import java.util.Map;
import java.util.Objects;

/**
 * @author Ice2Faith
 * @date 2024/2/23 14:29
 * @desc
 */
public class SimpleEntry<K,V> implements Map.Entry<K,V> {
    protected K key;
    protected V value;

    public SimpleEntry() {
    }

    public SimpleEntry(K key) {
        this.key = key;
    }

    public SimpleEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public K getKey() {
        return this.key;
    }

    public K setKey(K key) {
        K ret=this.key;
        this.key=key;
        return ret;
    }

    @Override
    public V getValue() {
        return this.value;
    }

    @Override
    public V setValue(V value) {
        V ret=this.value;
        this.value=value;
        return ret;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof SimpleEntry)) {
            return false;
        }

        SimpleEntry<?,?> other = (SimpleEntry<?,?>) obj;
        return Objects.equals(key, other.key)
                && Objects.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        int h = Objects.hashCode(key);
        int l =  Objects.hashCode(value);
        return h | l;
    }

    @Override
    public String toString() {
        String simpleName = this.getClass().getSimpleName();
        return simpleName+"["+key+":"+value+"]";
    }
}
