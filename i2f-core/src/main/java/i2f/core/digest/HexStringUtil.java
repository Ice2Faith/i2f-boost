package i2f.core.digest;


import i2f.core.annotations.remark.Author;
import i2f.core.collection.Collections;
import i2f.core.str.Appender;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ltb
 * @date 2022/3/19 15:26
 * @desc
 */
@Author("i2f")
public class HexStringUtil {

    public static String toHexString(byte[] data){
        return toHexString(data,null);
    }
    public static String toHexString(byte[] data,String separator){
        Appender<StringBuilder> builder = Appender.builder();
        for (int i = 0; i < data.length; i++) {
            if(i!=0){
                if(separator!=null){
                    builder.add(separator);
                }
            }
            builder.addFormat("%02X",(int)(data[i]&0x0ff));
        }
        return builder.get();
    }
    public static String toOtcString(byte[] data){
        return toOtcString(data,null);
    }
    public static String toOtcString(byte[] data,String separator){
        Appender<StringBuilder> builder = Appender.builder();
        for (int i = 0; i < data.length; i++) {
            if(i!=0){
                if(separator!=null){
                    builder.add(separator);
                }
            }
            builder.addFormat("%03o",(int)(data[i]&0x0ff));
        }
        return builder.get();
    }
    public static String toBinString(byte[] data){
        return toBinString(data,null);
    }
    public static String toBinString(byte[] data,String separator){
        Appender<StringBuilder> builder = Appender.builder();
        for (int i = 0; i < data.length; i++) {
            if(i!=0){
                if(separator!=null){
                    builder.add(separator);
                }
            }
            int bt=data[i]&0x0ff;
            for (int j = 7; j >= 0; j--) {
                int dt=(bt>>>j)&0x01;
                builder.add(dt);
            }
        }
        return builder.get();
    }
    public static String toDecString(byte[] data){
        return toDecString(data,null);
    }
    public static String toDecString(byte[] data,String separator){
        Appender<StringBuilder> builder = Appender.builder();
        for (int i = 0; i < data.length; i++) {
            if(i!=0){
                if(separator!=null){
                    builder.add(separator);
                }
            }
            builder.addFormat("%03d",(int)(data[i]&0x0ff));

        }
        return builder.get();
    }
    public static byte[] parseHexString(String data){
        return parseHexString(data,null);
    }
    public static byte[] parseHexString(String data,String separator){
        List<String> parts=new ArrayList<>();
        if(separator!=null){
            Collections.collect(parts, data.split(separator));
        }else{
            int dlen=data.length();
            for (int i = 0; (i+2) <= dlen; i+=2) {
                String item=data.substring(i,i+2);
                parts.add(item);
            }
        }
        int size=parts.size();
        byte[] ret=new byte[size];
        for (int i = 0; i < size; i++) {
            try{
                int num=Integer.parseInt(parts.get(i),16);
                ret[i]=(byte)(num&0x0ff);
            }catch(Exception e){
                ret[i]=0;
            }
        }
        return ret;
    }
    public static byte[] parseOtcString(String data){
        return parseOtcString(data,null);
    }
    public static byte[] parseOtcString(String data,String separator){
        List<String> parts=new ArrayList<>();
        if(separator!=null){
            Collections.collect(parts, data.split(separator));
        }else{
            int dlen=data.length();
            for (int i = 0; (i+3) <= dlen; i+=3) {
                String item=data.substring(i,i+3);
                parts.add(item);
            }
        }
        int size=parts.size();
        byte[] ret=new byte[size];
        for (int i = 0; i < size; i++) {
            try{
                int num=Integer.parseInt(parts.get(i),8);
                ret[i]=(byte)(num&0x0ff);
            }catch(Exception e){
                ret[i]=0;
            }
        }
        return ret;
    }
    public static byte[] parseDecString(String data){
        return parseDecString(data,null);
    }
    public static byte[] parseDecString(String data,String separator){
        List<String> parts=new ArrayList<>();
        if(separator!=null){
            Collections.collect(parts, data.split(separator));
        }else{
            int dlen=data.length();
            for (int i = 0; (i+3) <= dlen; i+=3) {
                String item=data.substring(i,i+3);
                parts.add(item);
            }
        }
        int size=parts.size();
        byte[] ret=new byte[size];
        for (int i = 0; i < size; i++) {
            try{
                int num=Integer.parseInt(parts.get(i),10);
                ret[i]=(byte)(num&0x0ff);
            }catch(Exception e){
                ret[i]=0;
            }
        }
        return ret;
    }
    public static byte[] parseBinString(String data){
        return parseBinString(data,null);
    }
    public static byte[] parseBinString(String data,String separator){
        List<String> parts=new ArrayList<>();
        if(separator!=null){
            Collections.collect(parts, data.split(separator));
        }else{
            int dlen=data.length();
            for (int i = 0; (i+8) <= dlen; i+=8) {
                String item=data.substring(i,i+8);
                parts.add(item);
            }
        }
        int size=parts.size();
        byte[] ret=new byte[size];
        for (int i = 0; i < size; i++) {
            int num=0;
            String item=parts.get(i);
            if(item!=null){
                char[] arr=item.toCharArray();
                for (int j = 0; j < arr.length; j++) {
                    if(arr[j]=='1'){
                        num=(num<<1)|1;
                    }else if(arr[j]=='0'){
                        num=(num<<1)|0;
                    }
                }
            }
            ret[i]=(byte)(num&0x0ff);
        }
        return ret;
    }
}
