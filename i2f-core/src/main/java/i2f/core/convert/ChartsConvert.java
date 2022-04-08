package i2f.core.convert;

import i2f.core.convert.impl.ObjectKeyProvider;
import i2f.core.convert.impl.ObjectValueProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ltb
 * @date 2022/3/27 18:40
 * @desc
 */
public class ChartsConvert {

    public static Map<String,Object> list2Map(List list, String key){
        return list2Map(list,new ObjectKeyProvider(key));
    }
    public static<K,E> Map<K,E> list2Map(List<E> list,IKeyProvider<K,E> provider) {
        return list2Map(list,new HashMap<K,E>(),provider);
    }
    /**
     * 以List中的某个元素的某个值作为键，将一个list转换为一个map
     * 适用于前端按照指定的key获取列表中的值得情况
     * @param list
     * @param map
     * @param provider
     * @param <K>
     * @param <E>
     * @return
     */
    public static<K,E> Map<K,E> list2Map(List<E> list,Map<K,E> map,IKeyProvider<K,E> provider){
        if(list==null || map==null || provider==null){
            return map;
        }
        for(E item : list){
            K key= provider.getKey(item);
            map.put(key,item);
        }
        return map;
    }

    public static Map<String,List<Object>> list2AxisMap(List list,String ... axises){
        return list2AxisMap(list,new ObjectValueProvider<>(),axises);
    }
    public static<T,K,E> Map<K,List<Object>> list2AxisMap(List<T> list,IValueProvider<T,K> provider,K ... axises) {
        return list2AxisMap(list,new HashMap<K,List<Object>>(),provider,axises);
    }
    /**
     * 将list按照指定的axies系列转换为一个map
     * 适用于将一个列表的结果中不同字段，分别形成不同的系列
     * 用于在前端进行图表数据展示是使用
     * @param list
     * @param map
     * @param provider
     * @param axises
     * @param <T>
     * @param <K>
     * @param <E>
     * @return
     */
    public static<T,K,E> Map<K,List<Object>> list2AxisMap(List<T> list,Map<K,List<Object>> map,IValueProvider<T,K> provider,K ... axises){
        if(list==null || map==null || provider==null || axises==null || axises.length==0){
            return map;
        }
        for(K item : axises){
            if(!map.containsKey(item)){
                map.put(item,new ArrayList<>());
            }
        }
        for(T item : list){
            for(K key : axises){
                Object val=provider.get(item,key);
                map.get(key).add(val);
            }
        }
        return map;
    }

}
