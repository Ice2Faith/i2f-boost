package i2f.core.str;

import i2f.core.functional.common.IMapper;
import i2f.core.iterator.Iterators;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author ltb
 * @date 2022/5/7 16:43
 * @desc
 */
public class Appender<T extends Appendable> {
    protected volatile T appender;

    public Appender(T appender){
        this.appender = appender;
    }
    public static Appender<StringBuffer> buffer(){
        return new Appender<>(new StringBuffer());
    }
    public static Appender<StringBuilder> builder(){
        return new Appender<>(new StringBuilder());
    }
    public static String str(Object ... objs){
        return builder().adds(objs).get();
    }
    public static String sepStr(Object separator,Object ... objs){
        return builder().addsSep(separator,objs).get();
    }

    ////////////////////////////////////////////////////
    public Appender<T> set(Object obj){
        clear();
        return add(obj);
    }
    public String get(){
        return appender.toString();
    }

    @Override
    public String toString() {
        return get();
    }

    public Appender<T> clear(){
        return trunc(0);
    }
    public Appender<T> trunc(int len){
        if (appender instanceof StringBuilder) {
            StringBuilder builder = (StringBuilder) appender;
            builder.setLength(len);
        } else if (appender instanceof StringBuffer) {
            StringBuffer buffer = (StringBuffer) appender;
            buffer.setLength(len);
        }
        return this;
    }
    public Appender<T> add(Object obj){
        if (appender instanceof StringBuilder) {
            StringBuilder builder = (StringBuilder) appender;
            builder.append(obj);
        } else if (appender instanceof StringBuffer) {
            StringBuffer buffer = (StringBuffer) appender;
            buffer.append(obj);
        }
        return this;
    }
    public Appender<T> addLine(Object obj){
        add(obj);
        return line();
    }
    public String substr(int start){
        if (appender instanceof StringBuilder) {
            StringBuilder builder = (StringBuilder) appender;
            return builder.substring(start);
        } else if (appender instanceof StringBuffer) {
            StringBuffer buffer = (StringBuffer) appender;
            return buffer.substring(start);
        }
        return null;
    }
    public String substr(int start,int end){
        if (appender instanceof StringBuilder) {
            StringBuilder builder = (StringBuilder) appender;
            return builder.substring(start,end);
        } else if (appender instanceof StringBuffer) {
            StringBuffer buffer = (StringBuffer) appender;
            return buffer.substring(start,end);
        }
        return null;
    }
    public Appender<T> addStart(Object obj){
        return insert(0,obj);
    }
    public Appender<T> insert(int index,Object obj){
        if (appender instanceof StringBuilder) {
            StringBuilder builder = (StringBuilder) appender;
            builder.insert(index,obj);
        } else if (appender instanceof StringBuffer) {
            StringBuffer buffer = (StringBuffer) appender;
            buffer.insert(index,obj);
        }
        return this;
    }
    public Appender<T> del(int start,int end){
        if (appender instanceof StringBuilder) {
            StringBuilder builder = (StringBuilder) appender;
            builder.delete(start,end);
        } else if (appender instanceof StringBuffer) {
            StringBuffer buffer = (StringBuffer) appender;
            buffer.delete(start,end);
        }
        return this;
    }
    public int length(){
        if (appender instanceof StringBuilder) {
            StringBuilder builder = (StringBuilder) appender;
            return builder.length();
        } else if (appender instanceof StringBuffer) {
            StringBuffer buffer = (StringBuffer) appender;
            return buffer.length();
        }
        return -1;
    }
    public Appender<T> addRepeat(Object obj,int count){
        for (int i = 0; i < count; i++) {
            add(obj);
        }
        return this;
    }
    public Appender<T> addFormat(String format,Object ... args){
        String str=String.format(format,args);
        return add(str);
    }
    public Appender<T> addDateFormat(Date date, String format){
        SimpleDateFormat fmt=new SimpleDateFormat(format);
        return add(fmt.format(date));
    }
    public Appender<T> addDateFormat(LocalDate date, DateTimeFormatter formatter){
        return add(date.format(formatter));
    }
    public Appender<T> addDateFormat(LocalDateTime date,DateTimeFormatter formatter){
        return add(date.format(formatter));
    }
    public Appender<T> addDateFormat(LocalTime date,DateTimeFormatter formatter){
        return add(date.format(formatter));
    }
    public Appender<T> addDateFormat(LocalDate date, String format){
        return add(date.format(DateTimeFormatter.ofPattern(format)));
    }
    public Appender<T> addDateFormat(LocalDateTime date,String format){
        return add(date.format(DateTimeFormatter.ofPattern(format)));
    }
    public Appender<T> addDateFormat(LocalTime date,String format){
        return add(date.format(DateTimeFormatter.ofPattern(format)));
    }
    public Appender<T> line(){
        return add("\n");
    }
    public Appender<T> tab(){
        return add("\t");
    }
    public Appender<T> blank(){
        return add(" ");
    }
    public Appender<T> line(int count){
        for (int i = 0; i < count; i++) {
            line();
        }
        return this;
    }
    public Appender<T> tab(int count){
        for (int i = 0; i < count; i++) {
            tab();
        }
        return this;
    }
    public Appender<T> blank(int count){
        for (int i = 0; i < count; i++) {
            blank();
        }
        return this;
    }
    public Appender<T> addWhen(boolean condition,Object obj){
        if(condition){
            add(obj);
        }
        return this;
    }
    public Appender<T> addNotWhen(boolean condition,Object obj){
        if(!condition){
            add(obj);
        }
        return this;
    }
    public Appender<T> addsWhen(boolean condition,Object ... arr){
        if(condition){
            adds(arr);
        }
        return this;
    }
    public Appender<T> addsNotWhen(boolean condition,Object ... arr){
        if(!condition){
            adds(arr);
        }
        return this;
    }
    public Appender<T> addIteratorWhen(boolean condition,Iterator<?> iterator){
        if(condition){
            addIterator(iterator);
        }
        return this;
    }
    public Appender<T> addIteratorNotWhen(boolean condition,Iterator<?> iterator){
        if(!condition){
            addIterator(iterator);
        }
        return this;
    }

    public Appender<T> keepEnd(String str){
        str=str+"";
        int slen=str.length();
        int clen=length();
        if(clen>=slen){
            String end=substr(clen-slen);
            if(!end.equals(str)){
                add(str);
            }
        }else{
            add(str);
        }
        return this;
    }
    public Appender<T> trimEnd(String str){
        str=str+"";
        int slen=str.length();
        int clen=length();
        if(clen>=slen){
            String end=substr(clen-slen);
            if(end.equals(str)){
                del(clen-slen,clen);
            }
        }
        return this;
    }

    public Appender<T> keepStart(String str){
        str=str+"";
        int slen=str.length();
        int clen=length();
        if(clen>=slen){
            String end=substr(0,slen);
            if(!end.equals(str)){
                addStart(str);
            }
        }else{
            addStart(str);
        }
        return this;
    }
    public Appender<T> trimStart(String str){
        str=str+"";
        int slen=str.length();
        int clen=length();
        if(clen>=slen){
            String end=substr(0,slen);
            if(end.equals(str)){
                del(0,slen);
            }
        }
        return this;
    }

    public Appender<T> addWhenEnd(String str,Object obj){
        str=str+"";
        int slen=str.length();
        int clen=length();
        if(clen>=slen){
            String end=substr(clen-slen);
            if(end.equals(str)){
                add(obj);
            }
        }
        return this;
    }
    public Appender<T> addNotWhenEnd(String str,Object obj){
        str=str+"";
        int slen=str.length();
        int clen=length();
        if(clen>=slen){
            String end=substr(clen-slen);
            if(!end.equals(str)){
                add(obj);
            }
        }
        return this;
    }

    public Appender<T> addWhenTo(Object obj,boolean condition,Object replace){
        if(condition){
            add(replace);
        }else{
            add(obj);
        }
        return this;
    }
    public Appender<T> addNotWhenTo(Object obj,boolean condition,Object replace){
        if(!condition){
            add(replace);
        }else{
            add(obj);
        }
        return this;
    }

    public Appender<T> addNullTo(Object obj,Object replace){
        return addWhenTo(obj,obj==null,replace);
    }
    public Appender<T> addEmptyTo(String str,Object replace){
        return addWhenTo(str,str==null || "".equals(str),replace);
    }

    public<E> Appender<T> addIteratorElem(Iterator<E> iterator, Object separator, Object open, Object close, IElemAppend<T,E> mapper){
        if(open!=null){
            add(open);
        }
        boolean isFirst=true;
        while (iterator.hasNext()){
            if(!isFirst){
                if(separator!=null){
                    add(separator);
                }
            }
            E val=iterator.next();
            if(mapper!=null){
                mapper.append(this,val);
            }else{
                add(val);
            }
            isFirst=false;
        }
        if(close!=null){
            add(close);
        }
        return this;
    }
    public<E> Appender<T> addIterableElem(Iterable<E> col, Object separator, Object open, Object close, IElemAppend<T,E> mapper){
        return addIteratorElem(Iterators.of(col), separator, open, close, mapper);
    }
    public<E> Appender<T> addCollectionElem(Collection<E> col, Object separator, Object open, Object close, IElemAppend<T,E> mapper){
        return addIteratorElem(Iterators.of(col), separator, open, close, mapper);
    }
    public<E> Appender<T> addEnumerationElem(Enumeration<E> enums, Object separator, Object open, Object close, IElemAppend<T,E> mapper){
        return addIteratorElem(Iterators.of(enums), separator, open, close, mapper);
    }
    public<E> Appender<T> addArrayElem(E[] arr, Object separator, Object open, Object close, IElemAppend<T,E> mapper){
        return addIteratorElem(Iterators.of(arr), separator, open, close, mapper);
    }
    public<E> Appender<T> addReflectArrayElem(Object arr, Object separator, Object open, Object close, IElemAppend<T,E> mapper){
        return addIteratorElem(Iterators.of(arr), separator, open, close, mapper);
    }
    public<E> Appender<T> addsElem(Object separator, Object open, Object close, IElemAppend<T,E> mapper,E ... arr){
        return addIteratorElem(Iterators.of(arr), separator, open, close, mapper);
    }

    public Appender<T> addIterator(Iterator<?> iterator, Object separator, Object open, Object close, IMapper mapper) {
        if (open != null) {
            add(open);
        }
        boolean isFirst = true;
        while (iterator.hasNext()) {
            if (!isFirst) {
                if (separator != null) {
                    add(separator);
                }
            }
            Object val=iterator.next();
            if(mapper!=null){
                val = mapper.get(val);
            }
            add(val);
            isFirst=false;
        }
        if(close!=null){
            add(close);
        }
        return this;
    }

    public Appender<T> addIterator(Iterator<?> iterator, Object separator, Object open, Object close) {
        return addIterator(iterator, separator, open, close, null);
    }

    public Appender<T> addIterator(Iterator<?> iterator, Object separator) {
        return addIterator(iterator, separator, null, null);
    }

    public Appender<T> addIterator(Iterator<?> iterator) {
        return addIterator(iterator, null, null, null);
    }

    public Appender<T> addIterable(Iterable<?> col, Object separator, Object open, Object close, IMapper mapper) {
        return addIterator(Iterators.of(col), separator, open, close, mapper);
    }

    public Appender<T> addIterable(Iterable<?> col, Object separator, Object open, Object close) {
        return addIterator(Iterators.of(col), separator, open, close);
    }

    public Appender<T> addIterable(Iterable<?> col, Object separator) {
        return addIterator(Iterators.of(col), separator, null, null);
    }

    public Appender<T> addIterable(Iterable<?> col) {
        return addIterator(Iterators.of(col), null, null, null);
    }

    public Appender<T> addCollection(Collection<?> col, Object separator, Object open, Object close, IMapper mapper) {
        return addIterator(Iterators.of(col), separator, open, close, mapper);
    }

    public Appender<T> addCollection(Collection<?> col, Object separator, Object open, Object close) {
        return addIterator(Iterators.of(col), separator, open, close);
    }

    public Appender<T> addCollection(Collection<?> col, Object separator) {
        return addIterator(Iterators.of(col), separator, null, null);
    }

    public Appender<T> addCollection(Collection<?> col) {
        return addIterator(Iterators.of(col), null, null, null);
    }

    public Appender<T> addEnumeration(Enumeration<?> enu, Object separator, Object open, Object close, IMapper mapper) {
        return addIterator(Iterators.of(enu), separator, open, close, mapper);
    }

    public Appender<T> addEnumeration(Enumeration<?> enu, Object separator, Object open, Object close) {
        return addIterator(Iterators.of(enu), separator, open, close);
    }

    public Appender<T> addEnumeration(Enumeration<?> enu, Object separator) {
        return addIterator(Iterators.of(enu), separator, null, null);
    }

    public Appender<T> addEnumeration(Enumeration<?> enu) {
        return addIterator(Iterators.of(enu), null, null, null);
    }

    public <E> Appender<T> addArray(E[] arr, Object separator, Object open, Object close, IMapper mapper) {
        return addIterator(Iterators.of(arr), separator, open, close, mapper);
    }

    public <E> Appender<T> addArray(E[] arr, Object separator, Object open, Object close) {
        return addIterator(Iterators.of(arr), separator, open, close);
    }

    public <E> Appender<T> addArray(E[] arr, Object separator) {
        return addIterator(Iterators.of(arr), separator, null, null);
    }

    public <E> Appender<T> addArray(E[] arr) {
        return addIterator(Iterators.of(arr), null, null, null);
    }

    public Appender<T> addReflectArray(Object arr, Object separator, Object open, Object close, IMapper mapper) {
        return addIterator(Iterators.of(arr), separator, open, close, mapper);
    }

    public Appender<T> addReflectArray(Object arr, Object separator, Object open, Object close) {
        return addIterator(Iterators.of(arr), separator, open, close);
    }

    public Appender<T> addReflectArray(Object arr, Object separator) {
        return addIterator(Iterators.of(arr), separator, null, null);
    }

    public Appender<T> addReflectArray(Object arr) {
        return addIterator(Iterators.of(arr), null, null, null);
    }
    public Appender<T> addsLine(Object ... arr){
        adds(arr);
        return line();
    }
    public Appender<T> adds(Object ... arr){
        return addIterator(Iterators.of(arr), null, null, null);
    }

    public Appender<T> addsSepLine(Object separator, Object... arr) {
        addsSep(separator, arr);
        return line();
    }

    public Appender<T> addsSep(Object separator, Object... arr) {
        return addIterator(Iterators.of(arr), separator, null, null);
    }

    public Appender<T> addsFull(Object separator, Object open, Object close, Object... arr) {
        return addIterator(Iterators.of(arr), separator, open, close);
    }

    public Appender<T> addsFullMap(Object separator, Object open, Object close, IMapper mapper, Object... arr) {
        return addIterator(Iterators.of(arr), separator, open, close);
    }

    public <K, V> Appender<T> addMap(Map<K, V> map, Object kvSeparator, Object entrySeparator, Object open, Object close) {
        if (open != null) {
            add(open);
        }

        if (close != null) {
            add(close);
        }

        boolean isFirst=true;
        for(Map.Entry<K,V> item : map.entrySet()){
            if(!isFirst){
                if(entrySeparator!=null){
                    add(entrySeparator);
                }
            }
            add(item.getKey());
            if(kvSeparator!=null){
                add(kvSeparator);
            }
            add(item.getValue());
            isFirst=false;
        }

        return this;
    }
    public<K,V> Appender<T> addMap(Map<K,V> map,Object kvSeparator,Object entrySeparator){
        return addMap(map,kvSeparator,entrySeparator,null,null);
    }

    public Appender<T> addStrBytes(byte[] bytes){
        return add(new String(bytes));
    }
    public Appender<T> addStrBytes(byte[] bytes,String charset) throws UnsupportedEncodingException {
        return add(new String(bytes,charset));
    }
    public Appender<T> addHexBytes(byte[] bytes){
        return addHexBytes(bytes,"0x",",");
    }
    public Appender<T> addHexBytes(byte[] bytes,Object prefix,Object separator){
        for (int i = 0; i < bytes.length; i++) {
            if(i!=0){
                if(separator!=null){
                    add(separator);
                }
            }
            if(prefix!=null){
                add(prefix);
            }
            addFormat("%02X",bytes[i]&0x0ff);
        }
        return this;
    }
    public Appender<T> addOtcBytes(byte[] bytes){
        return addOtcBytes(bytes,"0",",");
    }
    public Appender<T> addOtcBytes(byte[] bytes,Object prefix,Object separator){
        for (int i = 0; i < bytes.length; i++) {
            if(i!=0){
                if(separator!=null){
                    add(separator);
                }
            }
            if(prefix!=null){
                add(prefix);
            }
            addFormat("%03o",bytes[i]&0x0ff);
        }
        return this;
    }
}
