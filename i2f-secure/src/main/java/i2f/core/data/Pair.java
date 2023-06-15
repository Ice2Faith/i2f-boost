package i2f.core.data;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Pair<K, V> {
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
}
