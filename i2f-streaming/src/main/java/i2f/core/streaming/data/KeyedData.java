package i2f.core.streaming.data;

/**
 * @author Ice2Faith
 * @date 2023/5/2 11:47
 * @desc
 */
public class KeyedData<K, V> {
    public K key;
    public V val;

    public KeyedData() {
    }

    public KeyedData(K key, V val) {
        this.key = key;
        this.val = val;
    }
}
