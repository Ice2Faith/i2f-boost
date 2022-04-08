package i2f.core.map;

import i2f.core.annotations.remark.Author;
import i2f.core.data.Pair;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ltb
 * @date 2022/3/22 19:32
 * @desc
 */
@Author("i2f")
public class MapUtil {
    public static<K,V> HashMap<K,V> hashMap(Object ... kvs){
        return toMap(new HashMap<K,V>(kvs.length>64? kvs.length/2 : 32),kvs);
    }
    public static<K,V> TreeMap<K,V> treeMap(Object ... kvs){
        return toMap(new TreeMap<K,V>(),kvs);
    }
    public static<K,V> ConcurrentHashMap<K,V> concurrentHashMap(Object ... kvs){
        return toMap(new ConcurrentHashMap<>(kvs.length>64? kvs.length/2 : 32),kvs);
    }
    public static<K,V,R extends Map<K,V>> R toMap(R map,Pair<K,V> ... pairs){
        if(map==null){
            return map;
        }
        for(int i=0;i< pairs.length;i++){
            Pair<K,V> pair= pairs[i];
            if(pair.getKey()==null){
                continue;
            }
            map.put(pair.getKey(), pair.getVal());
        }
        return map;
    }
    public static<K,V,R extends Map<K,V>> R toMap(R map,Iterable<Pair<K,V>> ite){
        if(map==null){
            return map;
        }
        Iterator<Pair<K,V>> iterator=ite.iterator();
        while(iterator.hasNext()){
            Pair<K,V> pair= iterator.next();
            if(pair.getKey()==null){
                continue;
            }
            map.put(pair.getKey(), pair.getVal());
        }
        return map;
    }
    public static<K,V,R extends Map<K,V>> R toMap(R map,K[] keys,V... values){
        if(map==null){
            return map;
        }
        if(keys==null || values==null){
            return map;
        }
        for(int i=0;i< keys.length;i++){
            if(i<values.length){
                map.put(keys[i],values[i]);
            }else{
                map.put(keys[i],null);
            }
        }
        return map;
    }
    public static<R extends Map> R toMap(R map,Object ... kvs){
        if(map==null){
            return map;
        }
        for(int i=0;i<kvs.length;i+=2){
            if(i+1< kvs.length){
                map.put(kvs[i],kvs[i+1]);
            }else{
                map.put(kvs[i],null);
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
