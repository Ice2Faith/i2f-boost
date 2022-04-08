package i2f.core.array;

import i2f.core.annotations.notice.Name;
import i2f.core.annotations.remark.Author;
import i2f.core.annotations.remark.Remark;
import i2f.core.check.CheckUtil;
import i2f.core.collection.CollectionUtil;

import java.lang.reflect.Array;
import java.util.*;

/**
 * @author ltb
 * @date 2022/3/22 19:32
 * @desc
 */
@Author("i2f")
@Remark("provide array operation")
public class ArrayUtil {
    @Remark("decision obj whether is Array type")
    public static boolean isArray(@Name("arr") Object arr){
        return CheckUtil.isArray(arr);
    }
    public static int length(@Name("arr")Object arr){
        return Array.getLength(arr);
    }
    public static<T> T get(@Name("arr")Object arr,@Name("index")int index){
        return (T)Array.get(arr,index);
    }
    public static void set(@Name("arr")Object arr,@Name("index")int index,@Name("val")Object val){
        Array.set(arr,index,val);
    }

    public static <T> T[] toArray(@Name("objs")T ... objs){
        return (T[])objs;
    }

    public static <T> T[] toArray(@Name("ite")Enumeration<T> ite,@Name("tarType")Class<? extends T[]> tarType){
        List<T> list=new ArrayList<>();
        CollectionUtil.toCollection(list,ite);
        return toArray(list,tarType);
    }

    public static <T> T[] toArray(@Name("ite")Iterable<T> ite,@Name("tarType")Class<? extends T[]> tarType){
        List<T> list=new ArrayList<>();
        CollectionUtil.toCollection(list,ite);
        return toArray(list,tarType);
    }


    public static <T> T[] toArray(@Name("col")Collection<T> col, @Name("tarType")Class<? extends T[]> tarType){
        Object[] arr=new Object[0];
        if(CheckUtil.isEmptyCollection(col)){
            return (T[]) Arrays.copyOf(arr,arr.length,tarType);
        }
        arr=new Object[col.size()];
        Iterator it=col.iterator();
        int ix=0;
        while (it.hasNext()){
            arr[ix]=it.next();
            ix++;
        }
        return (T[]) Arrays.copyOf(arr,arr.length,tarType);
    }

    public static <T> T[] copyArray(@Name("index")int index, @Name("len")int len, @Name("tarType")Class<? extends T[]> tarType,@Name("srcArr") T ... srcArr){
        List<T> list=CollectionUtil.toCollection(new ArrayList<>(),srcArr,index,len);
        return toArray(list,tarType);
    }

    public static <T,E> T[] copy(@Name("index")int index,@Name("len")int len,@Name("tarType")Class<? extends T[]> tarType,@Name("srcArr")E ... srcArr){
        Object[] ret=copyArray(index,len,Object[].class,srcArr);
        return (T[]) Arrays.copyOf(ret,ret.length);
    }


    public static <T> T[] toArrayKeys(@Name("map")Map<T,?> map, @Name("tarType")Class<? extends T[]> tarType){
        Object[] arr=new Object[0];
        if(CheckUtil.isEmptyMap(map)){
            return (T[]) Arrays.copyOf(arr,arr.length,tarType);
        }
        arr=new Object[map.size()];
        Iterator it=map.keySet().iterator();
        int ix=0;
        while (it.hasNext()){
            arr[ix]=it.next();
            ix++;
        }
        return (T[]) Arrays.copyOf(arr,arr.length,tarType);
    }
    public static <T,E> T[] toArrayValues(@Name("map")Map<?,T> map,@Name("tarType")Class<? extends E[]> tarType){
        Object[] arr=new Object[0];
        if(CheckUtil.isEmptyMap(map)){
            return (T[]) Arrays.copyOf(arr,arr.length,tarType);
        }
        arr=new Object[map.size()];
        Iterator it=map.keySet().iterator();
        int ix=0;
        while (it.hasNext()){
            arr[ix]=map.get(it.next());
            ix++;
        }
        return (T[]) Arrays.copyOf(arr,arr.length,tarType);
    }

    public static<T> T[] mergeArray(@Name("ites")Iterable<T> ... ites){
        List<T> list=CollectionUtil.mergeCollection(new LinkedList<T>(),ites);
        int size=list.size();
        Object[] ret=new Object[size];
        int i=0;
        for(T item : list){
            ret[i++]=item;
        }
        return (T[]) Arrays.copyOf(ret,size);
    }
    public static<T> T[] mergeArray(@Name("enums")Enumeration<T> ... enums){
        List<T> list=CollectionUtil.mergeCollection(new LinkedList<T>(),enums);
        int size=list.size();
        Object[] ret=new Object[size];
        int i=0;
        for(T item : list){
            ret[i++]=item;
        }
        return (T[]) Arrays.copyOf(ret,size);
    }
    public static <T> T[] mergeArray(@Name("arr1")T[] arr1,@Name("arr2")T... arr2){
        int size=arr1.length+arr2.length;
        Object[] ret=new Object[size];
        int i=0;
        for(T item : arr1){
            ret[i++]=item;
        }
        for(T item : arr2){
            ret[i++]=item;
        }
        return (T[]) Arrays.copyOf(ret,size);
    }
}
