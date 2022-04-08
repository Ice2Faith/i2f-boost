package i2f.core.collection;

import i2f.core.annotations.notice.CloudBe;
import i2f.core.annotations.notice.Nullable;
import i2f.core.annotations.remark.Author;
import i2f.core.collection.adapter.ArrayIteratorAdapter;
import i2f.core.collection.adapter.EnumerationIteratorAdapter;
import i2f.core.collection.adapter.IterableIteratorAdapter;
import i2f.core.data.Pair;
import i2f.core.interfaces.IFilter;
import i2f.core.interfaces.IMap;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author ltb
 * @date 2022/3/22 19:32
 * @desc
 */
@Author("i2f")
public class CollectionUtil {

    public static<T> ArrayList<T> arrayList(T ... data){
        return toCollection(new ArrayList<T>(data.length>32? data.length : 32),data);
    }
    public static<T> LinkedList<T> linkList(T ... data){
        return toCollection(new LinkedList<>(),data);
    }
    public static<T> Vector<T> vector(T ... data){
        return toCollection(new Vector<>(),data);
    }
    public static<T> HashSet<T> hashSet(T ... data){
        return toCollection(new HashSet<T>(data.length>32? data.length : 32),data);
    }
    public static<T> TreeSet<T> treeSet(T ... data){
        return toCollection(new TreeSet<T>(),data);
    }
    public static<T> LinkedHashSet<T> linkedHashSet(T ... data){
        return toCollection(new LinkedHashSet<T>(),data);
    }
    public static<T> CopyOnWriteArrayList<T> copyOnWriteArrayList(T ... data){
        return toCollection(new CopyOnWriteArrayList<T>(),data);
    }
    public static<T> CopyOnWriteArraySet<T> copyOnWriteArraySet(T ... data){
        return toCollection(new CopyOnWriteArraySet<T>(),data);
    }

    public static<T> ArrayList<T> arrayList(Iterable<T> data){
        return toCollection(new ArrayList<T>(),data);
    }
    public static<T> LinkedList<T> linkList(Iterable<T> data){
        return toCollection(new LinkedList<>(),data);
    }
    public static<T> Vector<T> vector(Iterable<T> data){
        return toCollection(new Vector<>(),data);
    }
    public static<T> HashSet<T> hashSet(Iterable<T> data){
        return toCollection(new HashSet<T>(),data);
    }
    public static<T> TreeSet<T> treeSet(Iterable<T> data){
        return toCollection(new TreeSet<T>(),data);
    }
    public static<T> LinkedHashSet<T> linkedHashSet(Iterable<T> data){
        return toCollection(new LinkedHashSet<T>(),data);
    }
    public static<T> CopyOnWriteArrayList<T> copyOnWriteArrayList(Iterable<T> data){
        return toCollection(new CopyOnWriteArrayList<T>(),data);
    }
    public static<T> CopyOnWriteArraySet<T> copyOnWriteArraySet(Iterable<T> data){
        return toCollection(new CopyOnWriteArraySet<T>(),data);
    }

    public static<T> ArrayList<T> arrayList(Enumeration<T> data){
        return toCollection(new ArrayList<T>(),data);
    }
    public static<T> LinkedList<T> linkList(Enumeration<T> data){
        return toCollection(new LinkedList<>(),data);
    }
    public static<T> Vector<T> vector(Enumeration<T> data){
        return toCollection(new Vector<>(),data);
    }
    public static<T> HashSet<T> hashSet(Enumeration<T> data){
        return toCollection(new HashSet<T>(),data);
    }
    public static<T> TreeSet<T> treeSet(Enumeration<T> data){
        return toCollection(new TreeSet<T>(),data);
    }
    public static<T> LinkedHashSet<T> linkedHashSet(Enumeration<T> data){
        return toCollection(new LinkedHashSet<T>(),data);
    }
    public static<T> CopyOnWriteArrayList<T> copyOnWriteArrayList(Enumeration<T> data){
        return toCollection(new CopyOnWriteArrayList<T>(),data);
    }
    public static<T> CopyOnWriteArraySet<T> copyOnWriteArraySet(Enumeration<T> data){
        return toCollection(new CopyOnWriteArraySet<T>(),data);
    }

    public static<T> ArrayList<T> arrayList(Iterator<T> data){
        return toCollection(new ArrayList<T>(),data);
    }
    public static<T> LinkedList<T> linkList(Iterator<T> data){
        return toCollection(new LinkedList<>(),data);
    }
    public static<T> Vector<T> vector(Iterator<T> data){
        return toCollection(new Vector<>(),data);
    }
    public static<T> HashSet<T> hashSet(Iterator<T> data){
        return toCollection(new HashSet<T>(),data);
    }
    public static<T> TreeSet<T> treeSet(Iterator<T> data){
        return toCollection(new TreeSet<T>(),data);
    }
    public static<T> LinkedHashSet<T> linkedHashSet(Iterator<T> data){
        return toCollection(new LinkedHashSet<T>(),data);
    }
    public static<T> CopyOnWriteArrayList<T> copyOnWriteArrayList(Iterator<T> data){
        return toCollection(new CopyOnWriteArrayList<T>(),data);
    }
    public static<T> CopyOnWriteArraySet<T> copyOnWriteArraySet(Iterator<T> data){
        return toCollection(new CopyOnWriteArraySet<T>(),data);
    }

    public static <T,E extends Collection<T>> E toCollection(E dst,T ... src){
        return toCollection(dst,src,0,-1);
    }
    public static <T,E extends Collection<T>> E toCollection(E dst,Iterable<T> src){
        return toCollection(dst,src,0,-1);
    }
    public static <T,E extends Collection<T>> E toCollection(E dst,Enumeration<T> src){
        return toCollection(dst,src,0,-1);
    }
    public static <T,E extends Collection<T>> E toCollection(E dst,Iterator<T> src){
        return toCollection(dst,src,0,-1);
    }

    public static <K,V,M extends Map<K,V>,E extends Collection<Pair<K,V>>> E toCollection(E dst,M map){
        if(map==null){
            return dst;
        }
        for(Map.Entry<K,V> item : map.entrySet()){
            Pair<K,V> pair=new Pair<>(item.getKey(),item.getValue());
            dst.add(pair);
        }
        return dst;
    }

    public static<T,E extends Collection<T>> E toCollection(E dst,T[] src,int from,int size){
        return toCollection(dst,src,from,size,null,null);
    }
    public static<T,E extends Collection<T>> E toCollection(E dst,Enumeration<T> src,int from,int size){
        return toCollection(dst,src,from,size,null,null);
    }
    public static<T,E extends Collection<T>> E toCollection(E dst,Iterable<T> src,int from,int size){
        return toCollection(dst,src,from,size,null,null);
    }
    public static<T,E extends Collection<T>> E toCollection(E dst,Iterator<T> src,int from,int size){
        return toCollection(dst,src,from,size,null,null);
    }

    public static<V,T,E extends Collection<T>> E toCollection(E dst, V[] src, int from, @CloudBe("-1") int size, @Nullable IFilter<V> filter, @Nullable IMap<V,T> mapper){
        return toCollection(dst,new ArrayIteratorAdapter<>(src),from,size,filter,mapper);
    }
    public static<V,T,E extends Collection<T>> E toCollection(E dst,Enumeration<V> src,int from,@CloudBe("-1") int size,@Nullable IFilter<V> filter,@Nullable IMap<V,T> mapper){
        return toCollection(dst,new EnumerationIteratorAdapter<>(src),from,size,filter,mapper);
    }
    public static<V,T,E extends Collection<T>> E toCollection(E dst,Iterable<V> src,int from,@CloudBe("-1") int size,@Nullable IFilter<V> filter,@Nullable IMap<V,T> mapper){
        return toCollection(dst,new IterableIteratorAdapter<>(src),from,size,filter,mapper);
    }
    public static<V,T,E extends Collection<T>> E toCollection(E dst,Iterator<V> src,int from,@CloudBe("-1") int size,@Nullable IFilter<V> filter,@Nullable IMap<V,T> mapper){
        if(dst==null || src==null){
            return dst;
        }
        Iterator<V> iterator=src;
        boolean isFilterNull=(filter==null);
        boolean isMapperNull=(mapper==null);
        int count=0;
        int index=0;
        while(iterator.hasNext()){
            V val= iterator.next();
            if(index>=from){
                if(size>0 && count>=size){
                    break;
                }
                if(isFilterNull || filter.choice(val)){
                    T sval=isMapperNull?((T)val):mapper.map(val);
                    dst.add(sval);
                }
                count++;
            }
            index++;
        }
        return dst;
    }

    public static<T,E extends Collection<T>> E mergeCollection(E dst,Iterator<T> ... srcs){
        for(Iterator<T> item : srcs){
            toCollection(dst,item);
        }
        return dst;
    }
    public static<T,E extends Collection<T>> E mergeCollection(E dst,Iterable<T> ... srcs){
        for(Iterable<T> item : srcs){
            toCollection(dst,new IterableIteratorAdapter<>(item));
        }
        return dst;
    }
    public static<T,E extends Collection<T>> E mergeCollection(E dst,Enumeration<T> ... srcs){
        for(Enumeration<T> item : srcs){
            toCollection(dst,new EnumerationIteratorAdapter<>(item));
        }
        return dst;
    }
    public static<T,E extends Collection<T>> E mergeCollection(E dst,T[] ... srcs){
        for(T[] item : srcs){
            toCollection(dst,new ArrayIteratorAdapter<>(item));
        }
        return dst;
    }
    public static<T,E extends Collection<T>> E mergeCollection(E dst,T[] src1,T... src2){
        toCollection(dst,new ArrayIteratorAdapter<>(src1));
        toCollection(dst,new ArrayIteratorAdapter<>(src2));
        return dst;
    }
}
