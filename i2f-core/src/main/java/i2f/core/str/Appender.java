package i2f.core.str;

import i2f.core.collection.adapter.ArrayIteratorAdapter;
import i2f.core.collection.adapter.EnumerationIteratorAdapter;
import i2f.core.collection.adapter.IterableIteratorAdapter;
import i2f.core.collection.adapter.ReflectArrayIteratorAdapter;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;

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
    ////////////////////////////////////////////////////
    public Appender<T> set(Object obj){
        clear();
        return add(obj);
    }
    public String get(){
        return appender.toString();
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
    public Appender<T> line(){
        return add("\n");
    }
    public Appender<T> tab(){
        return add("\n");
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

    public Appender<T> adds(Iterator<?> iterator,Object separator,Object open,Object close){
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
            add(iterator.next());
            isFirst=false;
        }
        if(close!=null){
            add(close);
        }
        return this;
    }
    public Appender<T> adds(Iterator<?> iterator,Object separator){
        return adds(iterator,separator,null,null);
    }
    public Appender<T> adds(Iterator<?> iterator){
        return adds(iterator,null,null,null);
    }
    public Appender<T> addIterable(Iterable<?> col,Object separator,Object open,Object close){
        return adds(new IterableIteratorAdapter<>(col),separator,open,close);
    }
    public Appender<T> addCollection(Collection<?> col, Object separator, Object open, Object close){
        return adds(new IterableIteratorAdapter<>(col),separator,open,close);
    }
    public Appender<T> addEnumeration(Enumeration<?> enu,Object separator,Object open,Object close){
        return adds(new EnumerationIteratorAdapter<>(enu),separator,open,close);
    }
    public<E> Appender<T> addArray(E[] arr,Object separator,Object open,Object close){
        return adds(new ArrayIteratorAdapter<>(arr),separator,open,close);
    }
    public Appender<T> addReflectArray(Object arr,Object separator,Object open,Object close){
        return adds(new ReflectArrayIteratorAdapter<>(arr),separator,open,close);
    }
    public Appender<T> addArgsArray(Object separator,Object open,Object close,Object ... arr){
        return adds(new ArrayIteratorAdapter<>(arr),separator,open,close);
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
