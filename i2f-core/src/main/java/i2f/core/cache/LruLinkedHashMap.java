package i2f.core.cache;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author ltb
 * @date 2022/4/2 17:50
 * @desc
 */
public class LruLinkedHashMap<K,V> extends LinkedHashMap<K,V> {
    private int capital;
    public LruLinkedHashMap(int capital){
        super(32,0.75f,true);
        this.capital=capital;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size()>capital;
    }
}
