package i2f.core.map;

import i2f.core.annotations.notice.Nullable;
import i2f.core.annotations.remark.Author;
import i2f.core.collection.Collections;
import i2f.core.data.Pair;
import i2f.core.tuple.Tuples;
import i2f.core.tuple.impl.Tuple2;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ltb
 * @date 2022/3/22 19:32
 * @desc
 */
@Author("i2f")
public class Maps {
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || !map.keySet().iterator().hasNext();
    }

    public static <K, V, MAP extends Map<K, V>> MAP collect(MAP map, Tuple2<K, V>... kvs) {
        for (Tuple2<K, V> kv : kvs) {
            map.put(kv.t1, kv.t2);
        }
        return map;
    }

    public static <K, V, MAP extends Map<K, V>> MAP collect(MAP map, Map.Entry<K, V>... kvs) {
        for (Map.Entry<K, V> kv : kvs) {
            map.put(kv.getKey(), kv.getValue());
        }
        return map;
    }

    public static <K, V, MAP extends Map<K, V>> MAP valueAs(MAP map, V val, Iterator<K> keys) {
        while (keys.hasNext()) {
            map.put(keys.next(), val);
        }
        return map;
    }

    public static <K, V, IN extends Map<K, V>, OUT extends Map<V, K>, KEYS extends Collection<K>> OUT swapKv(IN inMap, OUT outMap, @Nullable KEYS col) {
        for (Map.Entry<K, V> item : inMap.entrySet()) {
            if (item.getValue() == null) {
                if (col != null) {
                    col.add(item.getKey());
                }
            } else {
                outMap.put(item.getValue(), item.getKey());
            }
        }
        return outMap;
    }

    public static <K, V> HashMap<K, V> hashMap(Tuple2<K, V>... kvs) {
        return collect(new HashMap<K, V>(i2f.core.collection.Collections.hashSize(kvs.length)), kvs);
    }

    public static <K extends Comparable<K>, V> TreeMap<K, V> treeMap(Tuple2<K, V>... kvs) {
        return collect(new TreeMap<K, V>(), kvs);
    }

    public static <K, V> LinkedHashMap<K, V> linkedHashMap(Tuple2<K, V>... kvs) {
        return collect(new LinkedHashMap<K, V>(), kvs);
    }

    public static <K, V> ConcurrentHashMap<K, V> concurrentHashMap(Tuple2<K, V>... kvs) {
        return collect(new ConcurrentHashMap<K, V>(Collections.hashSize(kvs.length)), kvs);
    }

    public static <K, V, MAP extends Map<K, V>> MAP collect(MAP map, K k1, V v1) {
        return collect(map, Tuples.of(k1, v1));
    }

    public static <K, V, MAP extends Map<K, V>> MAP collect(MAP map, K k1, V v1, K k2, V v2) {
        return collect(map, Tuples.of(k1, v1)
                , Tuples.of(k2, v2)
        );
    }

    public static <K, V, MAP extends Map<K, V>> MAP collect(MAP map, K k1, V v1, K k2, V v2, K k3, V v3) {
        return collect(map, Tuples.of(k1, v1)
                , Tuples.of(k2, v2)
                , Tuples.of(k3, v3)
        );
    }

    public static <K, V, MAP extends Map<K, V>> MAP collect(MAP map, K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        return collect(map, Tuples.of(k1, v1)
                , Tuples.of(k2, v2)
                , Tuples.of(k3, v3)
                , Tuples.of(k4, v4)
        );
    }

    public static <K, V, MAP extends Map<K, V>> MAP collect(MAP map, K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        return collect(map, Tuples.of(k1, v1)
                , Tuples.of(k2, v2)
                , Tuples.of(k3, v3)
                , Tuples.of(k4, v4)
                , Tuples.of(k5, v5)
        );
    }

    public static <K, V, MAP extends Map<K, V>> MAP collect(MAP map, K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
        return collect(map, Tuples.of(k1, v1)
                , Tuples.of(k2, v2)
                , Tuples.of(k3, v3)
                , Tuples.of(k4, v4)
                , Tuples.of(k5, v5)
                , Tuples.of(k6, v6)
        );
    }

    public static <K, V, MAP extends Map<K, V>> MAP collect(MAP map, K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7) {
        return collect(map, Tuples.of(k1, v1)
                , Tuples.of(k2, v2)
                , Tuples.of(k3, v3)
                , Tuples.of(k4, v4)
                , Tuples.of(k5, v5)
                , Tuples.of(k6, v6)
                , Tuples.of(k7, v7)
        );
    }

    public static <K, V, MAP extends Map<K, V>> MAP collect(MAP map, K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7,
                                                            K k8, V v8) {
        return collect(map, Tuples.of(k1, v1)
                , Tuples.of(k2, v2)
                , Tuples.of(k3, v3)
                , Tuples.of(k4, v4)
                , Tuples.of(k5, v5)
                , Tuples.of(k6, v6)
                , Tuples.of(k7, v7)
                , Tuples.of(k8, v8)
        );
    }

    public static <K, V, MAP extends Map<K, V>> MAP collect(MAP map, K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7,
                                                            K k8, V v8, K k9, V v9) {
        return collect(map, Tuples.of(k1, v1)
                , Tuples.of(k2, v2)
                , Tuples.of(k3, v3)
                , Tuples.of(k4, v4)
                , Tuples.of(k5, v5)
                , Tuples.of(k6, v6)
                , Tuples.of(k7, v7)
                , Tuples.of(k8, v8)
                , Tuples.of(k9, v9)
        );
    }

    public static <K, V, MAP extends Map<K, V>> MAP collect(MAP map, K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7,
                                                            K k8, V v8, K k9, V v9, K k10, V v10) {
        return collect(map, Tuples.of(k1, v1)
                , Tuples.of(k2, v2)
                , Tuples.of(k3, v3)
                , Tuples.of(k4, v4)
                , Tuples.of(k5, v5)
                , Tuples.of(k6, v6)
                , Tuples.of(k7, v7)
                , Tuples.of(k8, v8)
                , Tuples.of(k9, v9)
                , Tuples.of(k10, v10)
        );
    }

    public static <K, V, MAP extends Map<K, V>> MAP collect(MAP map, K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7,
                                                            K k8, V v8, K k9, V v9, K k10, V v10, K k11, V v11) {
        return collect(map, Tuples.of(k1, v1)
                , Tuples.of(k2, v2)
                , Tuples.of(k3, v3)
                , Tuples.of(k4, v4)
                , Tuples.of(k5, v5)
                , Tuples.of(k6, v6)
                , Tuples.of(k7, v7)
                , Tuples.of(k8, v8)
                , Tuples.of(k9, v9)
                , Tuples.of(k10, v10)
                , Tuples.of(k11, v11)
        );
    }

    public static <K, V, MAP extends Map<K, V>> MAP collect(MAP map, K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7,
                                                            K k8, V v8, K k9, V v9, K k10, V v10, K k11, V v11, K k12, V v12) {
        return collect(map, Tuples.of(k1, v1)
                , Tuples.of(k2, v2)
                , Tuples.of(k3, v3)
                , Tuples.of(k4, v4)
                , Tuples.of(k5, v5)
                , Tuples.of(k6, v6)
                , Tuples.of(k7, v7)
                , Tuples.of(k8, v8)
                , Tuples.of(k9, v9)
                , Tuples.of(k10, v10)
                , Tuples.of(k11, v11)
                , Tuples.of(k12, v12)
        );
    }

    public static <K, V, MAP extends Map<K, V>> MAP collect(MAP map, K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7,
                                                            K k8, V v8, K k9, V v9, K k10, V v10, K k11, V v11, K k12, V v12, K k13, V v13) {
        return collect(map, Tuples.of(k1, v1)
                , Tuples.of(k2, v2)
                , Tuples.of(k3, v3)
                , Tuples.of(k4, v4)
                , Tuples.of(k5, v5)
                , Tuples.of(k6, v6)
                , Tuples.of(k7, v7)
                , Tuples.of(k8, v8)
                , Tuples.of(k9, v9)
                , Tuples.of(k10, v10)
                , Tuples.of(k11, v11)
                , Tuples.of(k12, v12)
                , Tuples.of(k13, v13)
        );
    }

    public static <K, V, MAP extends Map<K, V>> MAP collect(MAP map, K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7,
                                                            K k8, V v8, K k9, V v9, K k10, V v10, K k11, V v11, K k12, V v12, K k13, V v13, K k14, V v14) {
        return collect(map, Tuples.of(k1, v1)
                , Tuples.of(k2, v2)
                , Tuples.of(k3, v3)
                , Tuples.of(k4, v4)
                , Tuples.of(k5, v5)
                , Tuples.of(k6, v6)
                , Tuples.of(k7, v7)
                , Tuples.of(k8, v8)
                , Tuples.of(k9, v9)
                , Tuples.of(k10, v10)
                , Tuples.of(k11, v11)
                , Tuples.of(k12, v12)
                , Tuples.of(k13, v13)
                , Tuples.of(k14, v14)
        );
    }

    public static <K, V, MAP extends Map<K, V>> MAP collect(MAP map, K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7,
                                                            K k8, V v8, K k9, V v9, K k10, V v10, K k11, V v11, K k12, V v12, K k13, V v13, K k14, V v14,
                                                            K k15, V v15) {
        return collect(map, Tuples.of(k1, v1)
                , Tuples.of(k2, v2)
                , Tuples.of(k3, v3)
                , Tuples.of(k4, v4)
                , Tuples.of(k5, v5)
                , Tuples.of(k6, v6)
                , Tuples.of(k7, v7)
                , Tuples.of(k8, v8)
                , Tuples.of(k9, v9)
                , Tuples.of(k10, v10)
                , Tuples.of(k11, v11)
                , Tuples.of(k12, v12)
                , Tuples.of(k13, v13)
                , Tuples.of(k14, v14)
                , Tuples.of(k15, v15)
        );
    }


    public static <K, V> HashMap<K, V> hashMap(Object... kvs) {
        return collect(new HashMap<K, V>(kvs.length > 64 ? kvs.length / 2 : 32), kvs);
    }

    public static <K, V> TreeMap<K, V> treeMap(Object... kvs) {
        return collect(new TreeMap<K, V>(), kvs);
    }

    public static <K, V> ConcurrentHashMap<K, V> concurrentHashMap(Object... kvs) {
        return collect(new ConcurrentHashMap<>(kvs.length > 64 ? kvs.length / 2 : 32), kvs);
    }

    public static <K, V, R extends Map<K, V>> R collect(R map, Pair<K, V>... pairs) {
        if (map == null) {
            return map;
        }
        for (int i = 0; i < pairs.length; i++) {
            Pair<K, V> pair = pairs[i];
            if (pair.getKey() == null) {
                continue;
            }
            map.put(pair.getKey(), pair.getVal());
        }
        return map;
    }

    public static <K, V, R extends Map<K, V>> R collect(R map, Iterable<Pair<K, V>> ite) {
        if (map == null) {
            return map;
        }
        Iterator<Pair<K, V>> iterator = ite.iterator();
        while (iterator.hasNext()) {
            Pair<K, V> pair = iterator.next();
            if (pair.getKey() == null) {
                continue;
            }
            map.put(pair.getKey(), pair.getVal());
        }
        return map;
    }

    public static <K, V, R extends Map<K, V>> R collect(R map, K[] keys, V... values) {
        if (map == null) {
            return map;
        }
        if (keys == null || values == null) {
            return map;
        }
        for (int i = 0; i < keys.length; i++) {
            if (i < values.length) {
                map.put(keys[i], values[i]);
            } else {
                map.put(keys[i], null);
            }
        }
        return map;
    }

    public static <R extends Map> R collect(R map, Object... kvs) {
        if (map == null) {
            return map;
        }
        for (int i = 0; i < kvs.length; i += 2) {
            if (i + 1 < kvs.length) {
                map.put(kvs[i], kvs[i + 1]);
            } else {
                map.put(kvs[i], null);
                break;
            }
        }
        return map;
    }

    public static<R extends Map> R removeNullValues(R map){
        if(map==null){
            return map;
        }
        List list=new ArrayList<>();
        for(Object item : map.entrySet()){
            Map.Entry<? extends Object,? extends Object> entry=(Map.Entry<? extends Object,? extends Object>)item;
            if(entry.getValue()==null){
                list.add(entry.getKey());
            }
        }
        for(Object item : list){
            map.remove(item);
        }
        return map;
    }

    public static<R extends Map> R emptyValues2Null(R map){
        if(map==null){
            return map;
        }
        for(Object item : map.entrySet()){
            Map.Entry<? extends Object,? extends Object> entry=(Map.Entry<? extends Object,? extends Object>)item;
            if(entry.getValue()!=null && entry.getValue() instanceof String){
                String str=(String)entry.getValue();
                if("".equals(str)){
                    map.put(entry.getKey(),null);
                }
            }
        }

        return map;
    }
}
