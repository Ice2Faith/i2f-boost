package i2f.core.data;

import i2f.core.annotations.remark.Author;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Author("i2f")
@Data
@NoArgsConstructor
public class Pair<K, V> implements Map.Entry<K, V> {
    public K key;
    public V val;
    public Object tag;

    public Pair(K key, V val) {
        this.key = key;
        this.val = val;
    }

    public Pair(K key, V val, Object tag) {
        this.key = key;
        this.val = val;
        this.tag = tag;
    }

    public static <TK, TV> Pair<TK, TV> of(TK key, TV val) {
        return new Pair<>(key, val);
    }

    @Override
    public V getValue() {
        return this.val;
    }

    @Override
    public V setValue(V value) {
        V old = this.val;
        this.val = value;
        return old;
    }
}
